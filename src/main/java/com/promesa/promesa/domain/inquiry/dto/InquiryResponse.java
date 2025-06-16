package com.promesa.promesa.domain.inquiry.dto;

public record InquiryResponse(
        Long inquiryId,
        String question,
        String answer
) {}
