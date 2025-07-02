package com.promesa.promesa.domain.review.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class ReviewNotFoundException extends PromesaCodeException {
    public static PromesaCodeException EXCEPTION = new ReviewNotFoundException();
    private ReviewNotFoundException() {
        super(ReviewErrorCode.REVIEW_NOT_FOUND);
    }
}