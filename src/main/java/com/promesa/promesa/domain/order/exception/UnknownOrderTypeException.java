package com.promesa.promesa.domain.order.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class UnknownOrderTypeException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new UnknownOrderTypeException();

    private UnknownOrderTypeException() {
        super(OrderErrorCode.UNKNOWN_ORDER_TYPE);
    }
}
