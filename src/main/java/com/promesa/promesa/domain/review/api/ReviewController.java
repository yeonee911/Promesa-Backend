package com.promesa.promesa.domain.review.api;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.common.dto.s3.PresignedUrlRequest;
import com.promesa.promesa.common.dto.s3.PresignedUrlResponse;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.review.application.ReviewService;
import com.promesa.promesa.domain.review.dto.request.AddReviewRequest;
import com.promesa.promesa.domain.review.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final S3Service s3Service;

    @PostMapping("/review-images/presigned-url")
    public ResponseEntity<List<PresignedUrlResponse>> getPresignedPutUrls(@RequestBody PresignedUrlRequest request) {
        return ResponseEntity.ok(reviewService.getPresignedPutUrls(request));
    }

    @DeleteMapping("/review-images")
    public ResponseEntity<Void> deleteReviewImage(
            @RequestParam String key
    ){
        reviewService.deleteReviewImage(key);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> createReview(
            @RequestBody AddReviewRequest request,
            @RequestParam String imageId,
            @AuthenticationPrincipal Member member
    )
    {

    }

}