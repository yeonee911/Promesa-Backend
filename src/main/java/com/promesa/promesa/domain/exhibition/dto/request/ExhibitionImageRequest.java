package com.promesa.promesa.domain.exhibition.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExhibitionImageRequest(
        @NotBlank(message = "이미지 키는 필수입니다")
        String key,

        @NotNull
        @Min(value = 0, message = "정렬 순서는 0 이상의 정수여야 합니다")
        int sortOrder
) {}
