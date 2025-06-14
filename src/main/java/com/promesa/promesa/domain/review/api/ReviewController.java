package com.promesa.promesa.domain.review.api;

import com.promesa.promesa.common.dto.s3.PresignedUrlRequest;
import com.promesa.promesa.common.dto.s3.PresignedUrlResponse;
import com.promesa.promesa.domain.review.application.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review-images/presigned-url")
    public ResponseEntity<List<PresignedUrlResponse>> getPresignedPutUrls(@RequestBody PresignedUrlRequest request) {
        return ResponseEntity.ok(reviewService.getPresignedPutUrls(request));
    }
}