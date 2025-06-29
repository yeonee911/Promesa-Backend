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
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
            @AuthenticationPrincipal OAuth2User user
    )
    {
        // 임시 유저 객체 찾기
        String provider = (String) user.getAttribute("provider");
        String providerId = (String) user.getAttribute("providerId");
        Member member = memberRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        ReviewResponse response = reviewService.addReview(request, itemId, member);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{itemId}/reviews/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    public ResponseEntity<String> updateReview(
            @PathVariable Long itemId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal OAuth2User user
    )
    {
        // 임시 유저 객체 찾기
        String provider = (String) user.getAttribute("provider");
        String providerId = (String) user.getAttribute("providerId");
        Member member = memberRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        reviewService.deleteReview(itemId, reviewId, member);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/items/{itemId}/reviews/{reviewId}")
    @Operation(summary = "리뷰 수정")
    public ResponseEntity<ReviewResponse> removeReview(
            @RequestBody @Valid UpdateReviewRequest request,
            @PathVariable Long itemId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal OAuth2User user
    )
    {
        // 임시 유저 객체 찾기
        String provider = (String) user.getAttribute("provider");
        String providerId = (String) user.getAttribute("providerId");
        Member member = memberRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        ReviewResponse response = reviewService.updateReview(request, itemId, reviewId, member);
        return ResponseEntity.ok(response);
    }
}