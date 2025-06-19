package com.promesa.promesa.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AddReviewRequest(
        @NotBlank
        @Size(min = 10, max = 1000)
        String content,

        int rating,
        List<String> imageKeys
){}