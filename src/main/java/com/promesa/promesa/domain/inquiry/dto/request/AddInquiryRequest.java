package com.promesa.promesa.domain.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddInquiryRequest(
        @NotNull
        Long artistId,

        @NotBlank(message = "질문은 필수입니다.")
        String question,

        @NotBlank(message = "답변은 필수입니다.")
        String answer
) {}
