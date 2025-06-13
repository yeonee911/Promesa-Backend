package com.promesa.promesa.domain.home.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.common.query.ItemQueryRepository;
import com.promesa.promesa.domain.home.dao.ExhibitionRepository;
import com.promesa.promesa.domain.home.dto.BrandInfoResponse;
import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import com.promesa.promesa.domain.home.exception.ExhibitionNotFoundException;
import com.promesa.promesa.domain.item.domain.Exhibition;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final ItemQueryRepository itemQueryRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final MemberRepository memberRepository;

    private final S3Service s3Service;

    private static final String BUCKET = "ceos-promesa";
    private static final String MAIN_IMAGE = "brand-info/mainImage.png";
    private static final String LEFT_IMAGE = "brand-info/leftImage.png";
    private static final String RIGHT_IMAGE = "brand-info/rightImage.png";

    /**
     * 메인 화면에서 브랜드 정보 반환
     * @return
     */
    public BrandInfoResponse getBrandInfo() {
        String mainUrl = s3Service.createPresignedGetUrl(BUCKET, MAIN_IMAGE);
        String leftUrl = s3Service.createPresignedGetUrl(BUCKET, LEFT_IMAGE);
        String rightUrl = s3Service.createPresignedGetUrl(BUCKET, RIGHT_IMAGE);
        return BrandInfoResponse.of(mainUrl, leftUrl, rightUrl);
    }

    public List<ItemPreviewResponse> getExhibitionItems(Long memberId, Long exhibitionId) {
        // 기획전 존재 여부 검증
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId)
                .orElseThrow(() -> ExhibitionNotFoundException.EXCEPTION);
        // member 존재 여부 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);

        return itemQueryRepository.findExhibitionItem(memberId, exhibitionId);
    }
}
