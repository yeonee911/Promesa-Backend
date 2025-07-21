package com.promesa.promesa.domain.delivery.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class DeliveryNotFoundException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new DeliveryNotFoundException();

    private DeliveryNotFoundException() {
        super(DeliveryErrorCode.DELIVERY_NOT_FOUND);
    }
}

