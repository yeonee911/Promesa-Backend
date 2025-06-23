package com.promesa.promesa.domain.review.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class ReviewAccessDeniedException extends PromesaCodeException {
    public static PromesaCodeException EXCEPTION = new ReviewAccessDeniedException();
    private ReviewAccessDeniedException() {
        super(ReviewErrorCode.REVIEW_ACCESS_DENIED);
    }
}