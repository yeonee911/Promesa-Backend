package com.promesa.promesa.domain.review.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class ReviewItemMismatchException extends PromesaCodeException {
    public static PromesaCodeException EXCEPTION = new ReviewItemMismatchException();
    private ReviewItemMismatchException() {
        super(ReviewErrorCode.REVIEW_ITEM_MISMATCH);
    }
}