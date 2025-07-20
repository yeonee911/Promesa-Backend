package com.promesa.promesa.domain.exhibition.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionResponse;
import com.promesa.promesa.domain.exhibition.query.ExhibitionQueryRepository;
import com.promesa.promesa.domain.item.query.ItemQueryRepository;
import com.promesa.promesa.domain.exhibition.dao.ExhibitionRepository;
import com.promesa.promesa.domain.exhibition.domain.Exhibition;
import com.promesa.promesa.domain.exhibition.domain.ExhibitionStatus;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionSummary;
import com.promesa.promesa.domain.exhibition.exception.ExhibitionNotFoundException;
import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final S3Service s3Service;
    private final ExhibitionQueryRepository exhibitionQueryRepository;
    private final ArtistRepository artistRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**
     * 특정 기획전 조회
     * @param exhibitionId
     * @param member
     * @return
     */
    public List<ItemPreviewResponse> getExhibitionItems(Member member, Long exhibitionId) {
        // 기획전 존재 여부 검증
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId)
                .orElseThrow(() -> ExhibitionNotFoundException.EXCEPTION);

        List<ItemPreviewResponse> responses = itemQueryRepository.findExhibitionItem(member, exhibitionId);

        return responses.stream()
                .map(r -> {
                    String imageUrl = s3Service.createPresignedGetUrl(bucketName, r.getImageUrl());
                    return ItemPreviewResponse.of(r, imageUrl);
                })
                .toList();
    }

    /**
     * 현재 진행 중인 기획전 조회 : 이미지는 presignedUrl로 반환
     * @return
     */
    public List<ExhibitionSummary> getOngoingExhibitions() {
        List<Exhibition> ongoing = exhibitionRepository.findAllByStatus(ExhibitionStatus.ONGOING);

        return ongoing.stream()
                .map(exhibition -> {
                    String imageUrl = s3Service.createPresignedGetUrl(bucketName, exhibition.getImageKey());
                    return ExhibitionSummary.of(exhibition, imageUrl);
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
                    String imageUrl = s3Service.createPresignedGetUrl(bucketName, exhibition.getImageKey());
                    return ExhibitionSummary.of(exhibition, imageUrl);
                })
                .toList();
    }

    public List<ExhibitionResponse> getExhibitions(ExhibitionStatus status, Member member) {
        List<Exhibition> exhibitions = exhibitionQueryRepository.findByStatusOrderByStartDateDesc(status);
        List<ExhibitionResponse> responses = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            String imageUrl = s3Service.createPresignedGetUrl(bucketName, exhibition.getImageKey());
            ExhibitionSummary summary = ExhibitionSummary.of(exhibition, imageUrl);

            List<ItemPreviewResponse> itemPreviews = itemQueryRepository.findExhibitionItem(member, exhibition.getId())
                    .stream()
                    .map(r -> {
                        String itemImageUrl = s3Service.createPresignedGetUrl(bucketName, r.getImageUrl());
                        return ItemPreviewResponse.of(r, itemImageUrl);
                    })
                    .toList();
            ExhibitionResponse response = ExhibitionResponse.of(summary, itemPreviews);
            responses.add(response);
        }

        return responses;
    }
}
