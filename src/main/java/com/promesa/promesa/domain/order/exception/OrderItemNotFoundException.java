package com.promesa.promesa.domain.order.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class OrderItemNotFoundException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new OrderItemNotFoundException();

    private OrderItemNotFoundException() {
        super(OrderErrorCode.ORDER_ITEM_NOT_FOUND);
    }
}
