package com.promesa.promesa.domain.inquiry.exception;

import com.promesa.promesa.common.exception.GlobalErrorCode;
import com.promesa.promesa.common.exception.PromesaCodeException;

public class ForbiddenInquiryAccessException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new ForbiddenInquiryAccessException();
    private ForbiddenInquiryAccessException() {
        super(InquiryErrorCode.FORBIDDEN_INQUIRY_ACCESS);
    }
}