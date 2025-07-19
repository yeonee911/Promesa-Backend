package com.promesa.promesa.domain.home.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.home.dto.response.BrandInfoResponse;
import com.promesa.promesa.domain.home.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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

    /**
     * 작가/작품 통합 검색
     * @param keyword
     * @return
     */
    public ResponseEntity<SearchResponse> search(String keyword) {
    }
}
