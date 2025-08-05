package com.promesa.promesa.domain.review.dto.response;

import com.promesa.promesa.domain.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReviewResponse {
    private Long reviewId;
    private String content;
    private Long reviewItemId;
    private Long reviewOrderItemId;
    private Long reviewerId;
    private String reviewerName;
    private int rating;
    private List<ReviewImageResponse> reviewImages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewResponse from(Review review, List<ReviewImageResponse> reviewImageResponse) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .reviewItemId(review.getItem().getId())
                .reviewOrderItemId(review.getOrderItem().getId())
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
                .reviewItemId(dto.getReviewItemId())
                .reviewOrderItemId(dto.getReviewOrderItemId())
                .reviewerId(dto.getReviewerId())
                .reviewerName(dto.getReviewerName())
                .rating(dto.getRating())
                .reviewImages(reviewImageResponse)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }


    public ReviewResponse(Long reviewId, String content, Long reviewItemId, Long reviewOrderItemId, Long reviewerId,
                          String reviewerName, int rating, List<ReviewImageResponse> reviewImages,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.reviewId = reviewId;
        this.content = content;
        this.reviewItemId = reviewItemId;
        this.reviewOrderItemId = reviewOrderItemId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.reviewImages = reviewImages;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setReviewImages(List<ReviewImageResponse> images) {
        this.reviewImages = images;
    }
}