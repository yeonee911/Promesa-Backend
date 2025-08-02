package com.promesa.promesa.domain.inquiry.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;
import com.promesa.promesa.domain.review.exception.ReviewErrorCode;

public class InquiryNotFoundException extends PromesaCodeException {
    public static PromesaCodeException EXCEPTION = new InquiryNotFoundException();
    private InquiryNotFoundException() {
        super(InquiryErrorCode.INQUIRY_NOT_FOUND);
    }
}