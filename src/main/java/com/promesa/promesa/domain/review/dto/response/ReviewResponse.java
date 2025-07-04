package com.promesa.promesa.domain.review.dto.response;

import com.promesa.promesa.domain.review.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
public class ReviewResponse {
    private final Long reviewId;
    private final String content;
    private final Long itemId;
    private final Long reviewerId;
    private final int rating;
    private final List<String> reviewImages;

    public static ReviewResponse from(Review review, List<String> imageKeys) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .itemId(review.getItem().getId())
                .reviewerId(review.getMember().getId())
                .rating(review.getRating())
                .reviewImages(imageKeys)
                .build();
    }

    public static ReviewResponse from(ReviewQueryDto dto, List<String> imageKeys) {
        return ReviewResponse.builder()
                .reviewId(dto.getReviewId())
                .content(dto.getContent())
                .itemId(dto.getItemId())
                .reviewerId(dto.getReviewerId())
                .rating(dto.getRating())
                .reviewImages(imageKeys)
                .build();
    }
}