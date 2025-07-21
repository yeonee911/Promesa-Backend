package com.promesa.promesa.domain.order.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class OrderNotFoundException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new OrderNotFoundException();

    private OrderNotFoundException() {
        super(OrderErrorCode.ORDER_NOT_FOUND);
    }
}
