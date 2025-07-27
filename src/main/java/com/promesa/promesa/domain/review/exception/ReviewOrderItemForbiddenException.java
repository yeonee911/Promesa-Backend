package com.promesa.promesa.domain.review.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class ReviewOrderItemForbiddenException extends PromesaCodeException {
    public static PromesaCodeException EXCEPTION = new ReviewOrderItemForbiddenException();
    private ReviewOrderItemForbiddenException() {
        super(ReviewErrorCode.REVIEW_ORDER_ITEM_FORBIDDEN);
    }
}