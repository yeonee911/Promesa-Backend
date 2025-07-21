package com.promesa.promesa.domain.order.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class InvalidOrderQuantityException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new InvalidOrderQuantityException();

    private InvalidOrderQuantityException() {
        super(OrderErrorCode.INVALID_QUANTITY);
    }
}
