package com.promesa.promesa.domain.review.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class ReviewItemNotDeliveredException extends PromesaCodeException {
    public static PromesaCodeException EXCEPTION = new ReviewItemNotDeliveredException();
    private ReviewItemNotDeliveredException() {
        super(ReviewErrorCode.REVIEW_ITEM_NOT_DELIVERED);
    }
}