package com.promesa.promesa.domain.exhibition.application;

import com.promesa.promesa.common.application.ImageService;
import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.exhibition.domain.*;
import com.promesa.promesa.domain.exhibition.dto.request.AddExhibitionRequest;
import com.promesa.promesa.domain.exhibition.dto.request.ExhibitionImageRequest;
import com.promesa.promesa.domain.exhibition.dto.response.*;
import com.promesa.promesa.domain.exhibition.exception.DuplicateExhibitionTitleException;
import com.promesa.promesa.domain.exhibition.exception.InvalidExhibitionDateException;
import com.promesa.promesa.domain.exhibition.query.ExhibitionQueryRepository;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.item.query.ItemQueryRepository;
import com.promesa.promesa.domain.exhibition.dao.ExhibitionRepository;
import com.promesa.promesa.domain.exhibition.exception.ExhibitionNotFoundException;
import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import com.promesa.promesa.domain.member.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final S3Service s3Service;
    private final ExhibitionQueryRepository exhibitionQueryRepository;
    private final ArtistRepository artistRepository;
    private final ImageService imageService;
    private final ItemRepository itemRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**
     * 특정 기획전 조회
     * @param exhibitionId
     * @param member
     * @return
     */
    public ExhibitionDetailResponse getExhibitionInfo(Member member, Long exhibitionId) {
        // 기획전 존재 여부 검증
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId)
                .orElseThrow(() -> ExhibitionNotFoundException.EXCEPTION);

        String thumbnailImageUrl = s3Service.createPresignedGetUrl(bucketName, exhibition.getThumbnailImageKey());
        List<String> artistNames = exhibitionQueryRepository.findArtistNames(exhibitionId);
        ExhibitionSummary summary = ExhibitionSummary.of(exhibition, artistNames, thumbnailImageUrl);

        List<ExhibitionImageResponse> detailedImageUrls = exhibition.getDetailImages().stream()
                .map(img -> {
                    String url = s3Service.createPresignedGetUrl(bucketName, img.getImageKey());
                    return new ExhibitionImageResponse(url, img.getSortOrder());
                })
                .toList();

        ExhibitionDetail detail = ExhibitionDetail.of(detailedImageUrls);

        List<ItemPreviewResponse> itemPreviews = itemQueryRepository.findExhibitionItem(member, exhibitionId)
                .stream()
                .map(r -> {
                    String imageUrl = s3Service.createPresignedGetUrl(bucketName, r.getImageUrl());
                    return ItemPreviewResponse.of(r, imageUrl);
                })
                .toList();
        ExhibitionDetailResponse response = ExhibitionDetailResponse.of(summary, detail, itemPreviews);
        return response;
    }

    /**
     * 현재 진행 중인 기획전 조회 : 이미지는 presignedUrl로 반환
     * @return
     */
    public List<ExhibitionSummary> getOngoingExhibitions() {
        List<Exhibition> ongoing = exhibitionRepository.findAllByStatus(ExhibitionStatus.ONGOING);

        return ongoing.stream()
                .map(exhibition -> {
                    String imageUrl = s3Service.createPresignedGetUrl(bucketName, exhibition.getThumbnailImageKey());
                    List<String> artistNames = exhibitionQueryRepository.findArtistNames(exhibition.getId());
                    return ExhibitionSummary.of(exhibition, artistNames, imageUrl);
                })
                .toList();
    }

    /**
     * 작가가 참여한 기획전을 조회
     * @param artistId
     * @return
     */
    public List<ExhibitionSummary> getExhibitionsByArtist(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);

        List<Exhibition> exhibitions = exhibitionQueryRepository.findByArtistId(artistId);

        return exhibitions.stream()
                .map(exhibition -> {
                    String imageUrl = s3Service.createPresignedGetUrl(bucketName, exhibition.getThumbnailImageKey());
                    List<String> artistNames = exhibitionQueryRepository.findArtistNames(exhibition.getId());
                    return ExhibitionSummary.of(exhibition, artistNames, imageUrl);
                })
                .toList();
    }

    public List<ExhibitionSummaryResponse> getExhibitions(ExhibitionStatus status, Member member) {
        List<Exhibition> exhibitions = exhibitionQueryRepository.findByStatusOrderByStartDateDesc(status);
        List<ExhibitionSummaryResponse> responses = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            String imageUrl = s3Service.createPresignedGetUrl(bucketName, exhibition.getThumbnailImageKey());
            List<String> artistNames = exhibitionQueryRepository.findArtistNames(exhibition.getId());
            ExhibitionSummary summary = ExhibitionSummary.of(exhibition, artistNames, imageUrl);

            List<ItemPreviewResponse> itemPreviews = itemQueryRepository.findExhibitionItem(member, exhibition.getId())
                    .stream()
                    .map(r -> {
                        String itemImageUrl = s3Service.createPresignedGetUrl(bucketName, r.getImageUrl());
                        return ItemPreviewResponse.of(r, itemImageUrl);
                    })
                    .toList();
            ExhibitionSummaryResponse response = ExhibitionSummaryResponse.of(summary, itemPreviews);
            responses.add(response);
        }

        return responses;
    }

    /**
     * 기획전 등록
     * @param request
     * @return
     */
    @Transactional
    public String createExhibition(@Valid AddExhibitionRequest request) {
        // title 중복 검사
        if (exhibitionRepository.existsByTitle(request.title())){
            throw DuplicateExhibitionTitleException.EXCEPTION;
        }

        LocalDate startDate = request.startDate();
        LocalDate endDate = request.endDate();
        ExhibitionStatus status = decideStatus(startDate, endDate);

        Exhibition  newExhibition = Exhibition.builder()
                .title(request.title())
                .description(request.description())
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .build();
        exhibitionRepository.save(newExhibition);
        Long exhibitionId = newExhibition.getId();

        // 썸네일 등록
        String thumbnailKey = imageService.transferImage(request.thumbnailKey(), exhibitionId);
        ExhibitionImage thumbnail = ExhibitionImage.builder()
                .imageKey(thumbnailKey)
                .sortOrder(1)   // 썸네일은 1장이라 순서 상관없음
                .isThumbnail(true)
                .build();
        newExhibition.addExhibitionImage(thumbnail);

        // 상세 이미지 등록
        for (ExhibitionImageRequest exhibitionImageRequest : request.detailImageKeys()) {
            int sortOrder = exhibitionImageRequest.sortOrder();
            String targetKey = imageService.transferImage(exhibitionImageRequest.key(), exhibitionId);
            ExhibitionImage newExhibitionImage = ExhibitionImage.builder()
                    .imageKey(targetKey)
                    .isThumbnail(false)
                    .sortOrder(sortOrder)
                    .build();
            newExhibition.addExhibitionImage(newExhibitionImage);
        }

        // 작품 목록 조회
        List<Item> items = itemRepository.findAllById(request.itemIds());
        if (items.size() != request.itemIds().size()) {
            throw ItemNotFoundException.EXCEPTION;
        }

        // 작가 목록 조회
        Set<Long> artistIds = items.stream()
                .map(item -> item.getArtist().getId())
                .collect(Collectors.toSet());
        List<Artist> artists = artistRepository.findAllById(artistIds);
        if (artists.size() != artistIds.size()) {
            throw ArtistNotFoundException.EXCEPTION;
        }
        Map<Long, Artist> artistMap = artists.stream()
                .collect(Collectors.toMap(Artist::getId, Function.identity()));

        Set<Long> addedArtistIds = new HashSet<>();
        for (Item item : items) {
            // ExhibitionItem
            ExhibitionItem exhibitionItem = ExhibitionItem.builder()
                    .exhibition(newExhibition)
                    .item(item)
                    .build();
            newExhibition.addExhibitionItem(exhibitionItem);

            // ExhibitionArtist (중복 방지)
            Long artistId = item.getArtist().getId();
            if (addedArtistIds.add(artistId)) {
                Artist artist = artistMap.get(artistId);
                ExhibitionArtist exhibitionArtist = ExhibitionArtist.builder()
                        .exhibition(newExhibition)
                        .artist(artist)
                        .build();
                newExhibition.addExhibitionArtist(exhibitionArtist);
            }
        }

        String message = "성공적으로 등록되었습니다.";
        return message;
    }

    public ExhibitionStatus decideStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        // 종료일이 시작일보다 빠르면 오류
        if (endDate != null && endDate.isBefore(startDate)) {
            throw InvalidExhibitionDateException.EXCEPTION;
        }

        ExhibitionStatus status;
        if (endDate != null) {  // 종료일이 있는 전시
            if (today.isBefore(startDate)) {
                status = ExhibitionStatus.UPCOMING;   // 오늘 < 시작
            }
            else if (!today.isAfter(endDate)) {
                status = ExhibitionStatus.ONGOING;    // 시작 ≤ 오늘 ≤ 종료
            }
            else {
                status = ExhibitionStatus.PAST;       // 오늘 > 종료
            }
        }
        else {  // 종료일이 없는 전시 → 시작 전/후로만 구분
            if (today.isBefore(startDate)) {
                status = ExhibitionStatus.UPCOMING;   // 오늘 < 시작
            }
            else {
                status = ExhibitionStatus.PERMANENT;  // 오늘 ≥ 시작
            }
        }
        return status;
    }
}
