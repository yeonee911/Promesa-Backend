package com.promesa.promesa.domain.review.dto.response;

import com.promesa.promesa.domain.review.domain.Review;

import java.util.List;

public record ReviewResponse(
        Long reviewId,
        String content,
        Long itemId,
        Long reviewerId,
        int rating,
        List<String> reviewImages
){
    public static ReviewResponse from(Review review, List<String> imageKeys) {
        return new ReviewResponse(
                review.getId(),
                review.getContent(),
                review.getItem().getId(),
                review.getMember().getId(),
                review.getRating(),
                imageKeys
        );
    }
}