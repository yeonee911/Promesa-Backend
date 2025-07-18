package com.promesa.promesa.domain.shippingAddress.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class ShippingAddressAlreadyExistsException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new ShippingAddressAlreadyExistsException();

    private ShippingAddressAlreadyExistsException() {
        super(ShippingAddressErrorCode.SHIPPING_ADDRESS_DUPLICATE);
    }
}
