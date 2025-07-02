package com.promesa.promesa.domain.review.api;

import com.promesa.promesa.common.dto.s3.PresignedUrlRequest;
import com.promesa.promesa.common.dto.s3.PresignedUrlResponse;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.review.application.ReviewImageService;
import com.promesa.promesa.domain.review.application.ReviewService;
import com.promesa.promesa.domain.review.dto.request.AddReviewRequest;
import com.promesa.promesa.domain.review.dto.request.UpdateReviewRequest;
import com.promesa.promesa.domain.review.dto.response.ReviewResponse;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;
    private final MemberRepository memberRepository;

    @PostMapping("/review-images/presigned-url")
    @Operation(summary = "이미지 업로드용 url 발급")
    public ResponseEntity<List<PresignedUrlResponse>> getPresignedPutUrls(@RequestBody PresignedUrlRequest request) {
        return ResponseEntity.ok(reviewImageService.getPresignedPutUrls(request));
    }

    @DeleteMapping("/review-images")
    @Operation(summary = "이미지 삭제")
    public ResponseEntity<Void> deleteReviewImage(
            @RequestParam String key
    ){
        reviewImageService.deleteReviewImage(key);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/items/{itemId}/reviews")
    @Operation(summary = "리뷰 등록")
    public ResponseEntity<ReviewResponse> createReview(
            @RequestBody @Valid AddReviewRequest request,
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomUserDetails user
    )
    {
        Member member = (user != null) ? user.getMember() : null;
        ReviewResponse response = reviewService.addReview(request, itemId, member);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{itemId}/reviews/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    public ResponseEntity<String> removeReview(
            @PathVariable Long itemId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails user
    )
    {
        Member member = (user != null) ? user.getMember() : null;
        reviewService.deleteReview(itemId, reviewId, member);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/items/{itemId}/reviews/{reviewId}")
    @Operation(summary = "리뷰 수정")
    public ResponseEntity<ReviewResponse> updateReview(
            @RequestBody @Valid UpdateReviewRequest request,
            @PathVariable Long itemId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails user
    )
    {
        Member member = (user != null) ? user.getMember() : null;
        ReviewResponse response = reviewService.updateReview(request, itemId, reviewId, member);
        return ResponseEntity.ok(response);
    }
}