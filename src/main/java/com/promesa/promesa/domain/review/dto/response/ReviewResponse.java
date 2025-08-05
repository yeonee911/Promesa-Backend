package com.promesa.promesa.domain.review.dto.response;

import com.promesa.promesa.domain.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponse {
    private final Long reviewId;
    private final String content;
    private final Long itemId;
    private final Long orderItemId;
    private final Long reviewerId;
    private final String reviewerName;
    private final int rating;
    private List<ReviewImageResponse> reviewImages;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewResponse from(Review review, List<ReviewImageResponse> reviewImageResponse) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .itemId(review.getItem().getId())
                .orderItemId(review.getOrderItem().getId())
                .reviewerId(review.getMember().getId())
                .reviewerName(review.getMember().getName())
                .rating(review.getRating())
                .reviewImages(reviewImageResponse)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public static ReviewResponse from(ReviewQueryDto dto, List<ReviewImageResponse> reviewImageResponse) {
        return ReviewResponse.builder()
                .reviewId(dto.getReviewId())
                .content(dto.getContent())
                .itemId(dto.getItemId())
                .orderItemId(dto.getOrderItemId())
                .reviewerId(dto.getReviewerId())
                .reviewerName(dto.getReviewerName())
                .rating(dto.getRating())
                .reviewImages(reviewImageResponse)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public void setReviewImages(List<ReviewImageResponse> images) {
        this.reviewImages = images;
    }
}