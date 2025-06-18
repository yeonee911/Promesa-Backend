package com.promesa.promesa.domain.review.dto.response;

import com.promesa.promesa.domain.review.domain.ReviewImage;

import java.util.List;

public record ReviewResponse(
        Long reviewId,
        String content,
        Long itemId,
        Long reviewerId,
        int rating,
        List<String> reviewImages
){}