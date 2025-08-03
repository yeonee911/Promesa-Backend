package com.promesa.promesa.domain.order.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class InvalidOrderStatusException extends PromesaCodeException{
    public static final PromesaCodeException EXCEPTION = new InvalidOrderStatusException();

    private InvalidOrderStatusException() {
        super(OrderErrorCode.INVALID_STATUS_TRANSITION);
    }
}
