package com.promesa.promesa.domain.inquiry.dto.response;

import com.promesa.promesa.domain.inquiry.domain.Inquiry;

public record InquiryResponse(
        Long inquiryId,
        String question,
        String answer
) {
    public static InquiryResponse of(Inquiry inquiry) {
        return new InquiryResponse(
                inquiry.getInquiryId(),
                inquiry.getQuestion(),
                inquiry.getAnswer()
        );
    }
}
