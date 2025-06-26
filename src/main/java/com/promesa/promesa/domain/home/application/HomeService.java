package com.promesa.promesa.domain.home.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.common.query.ItemQueryRepository;
import com.promesa.promesa.domain.exhibition.dao.ExhibitionRepository;
import com.promesa.promesa.domain.home.dto.BrandInfoResponse;
import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import com.promesa.promesa.domain.exhibition.exception.ExhibitionNotFoundException;
import com.promesa.promesa.domain.exhibition.domain.Exhibition;
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
public class HomeService {
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")  // application.yml 에 정의 필요
    private String bucketName;

    private static final String MAIN_IMAGE = "brand-info/mainImage.png";
    private static final String LEFT_IMAGE = "brand-info/leftImage.png";
    private static final String RIGHT_IMAGE = "brand-info/rightImage.png";

    /**
     * 메인 화면에서 브랜드 정보 반환
     * @return
     */
    public BrandInfoResponse getBrandInfo() {
        String mainUrl = s3Service.createPresignedGetUrl(bucketName, MAIN_IMAGE);
        String leftUrl = s3Service.createPresignedGetUrl(bucketName, LEFT_IMAGE);
        String rightUrl = s3Service.createPresignedGetUrl(bucketName, RIGHT_IMAGE);
        return BrandInfoResponse.of(mainUrl, leftUrl, rightUrl);
    }
}
