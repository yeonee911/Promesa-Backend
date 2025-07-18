package com.promesa.promesa.domain.review.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.common.dto.s3.PresignedUrlRequest;
import com.promesa.promesa.common.dto.s3.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewImageService {
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**
     * 이미지 업로드용 presigned url 생성
     * @param request
     * @return
     */
    public List<PresignedUrlResponse> getPresignedPutUrls(PresignedUrlRequest request) {

        return s3Service.createPresignedPutUrl(
                bucketName,
                request.imageType(),
                request.referenceId(),
                request.subType(),
                request.subReferenceId(),
                request.fileNames(),
                request.metadata()
        );
    }

    /**
     * 이미지 삭제
     * @param key
     */
    public void deleteReviewImage(String key) {
        s3Service.deleteObject(bucketName, key);
    }
}
