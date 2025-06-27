package com.promesa.promesa.domain.exhibition.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.exhibition.query.ExhibitionQueryRepository;
import com.promesa.promesa.domain.item.query.ItemQueryRepository;
import com.promesa.promesa.domain.exhibition.dao.ExhibitionRepository;
import com.promesa.promesa.domain.exhibition.domain.Exhibition;
import com.promesa.promesa.domain.exhibition.domain.ExhibitionStatus;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionResponse;
import com.promesa.promesa.domain.exhibition.exception.ExhibitionNotFoundException;
import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final MemberRepository memberRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final S3Service s3Service;
    private final ExhibitionQueryRepository exhibitionQueryRepository;

    @Value("${aws.s3.bucket}")  // application.yml 에 정의 필요
    private String bucketName;

    public List<ItemPreviewResponse> getExhibitionItems(Long memberId, Long exhibitionId) {
        // 기획전 존재 여부 검증
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId)
                .orElseThrow(() -> ExhibitionNotFoundException.EXCEPTION);
        // member 존재 여부 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);

        List<ItemPreviewResponse> responses = itemQueryRepository.findExhibitionItem(memberId, exhibitionId);

        // imageKey → presigned URL 변환
        responses.forEach(response -> {
            if (response.getImageKey() != null) {
                String url = s3Service.createPresignedGetUrl(bucketName, response.getImageKey());
                response.setImageUrl(url);
            }
        });
        return responses;
    }

    /**
     * 현재 진행 중인 기획전 조회 : 이미지는 presignedUrl로 반환
     * @return
     */
    public List<ExhibitionResponse> getOngoingExhibitions() {
        List<Exhibition> ongoing = exhibitionRepository.findAllByStatus(ExhibitionStatus.ONGOING);

        return ongoing.stream()
                .map(exhibition -> {
                    String imageUrl = s3Service.createPresignedGetUrl(bucketName, exhibition.getImageKey());
                    return ExhibitionResponse.of(exhibition, imageUrl);
                })
                .toList();
    }

    /**
     * 작가가 참여한 기획전을 조회
     * @param artistId
     * @return
     */
    public List<ExhibitionResponse> getExhibitionsByArtist(Long artistId) {
        List<Exhibition> exhibitions = exhibitionQueryRepository.findByArtistId(artistId);

        return exhibitions.stream()
                .map(exhibition -> {
                    String imageUrl = s3Service.createPresignedGetUrl(bucketName, exhibition.getImageKey());
                    return ExhibitionResponse.of(exhibition, imageUrl);
                })
                .toList();
    }
}
