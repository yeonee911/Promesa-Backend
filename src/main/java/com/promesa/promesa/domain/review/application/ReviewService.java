package com.promesa.promesa.domain.review.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.common.dto.s3.PresignedUrlRequest;
import com.promesa.promesa.common.dto.s3.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public List<PresignedUrlResponse> getPresignedPutUrls(PresignedUrlRequest request) {
        return s3Service.createPresignedPutUrl(
                bucketName,
                request.imageType(),
                request.referenceId(),
                request.fileNames(),
                request.metadata()
        );
    }

    public void deleteReviewImage(String key) {
        s3Service.deleteObject(bucketName, key);
    }
}