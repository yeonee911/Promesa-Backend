package com.promesa.promesa.domain.review.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class ReviewUnauthorizedException extends PromesaCodeException {
    public static PromesaCodeException EXCEPTION = new ReviewUnauthorizedException();
    private ReviewUnauthorizedException() {
        super(ReviewErrorCode.REVIEW_UNAUTHORIZED);
    }
}