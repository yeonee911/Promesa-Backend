package com.promesa.promesa.domain.review.dto.request;

import jakarta.validation.constraints.Size;

import java.util.List;

public record AddReviewRequest(
        @Size(min = 10, max = 255)
        String content,

        int rating,
        List<String> imageKeys
){}