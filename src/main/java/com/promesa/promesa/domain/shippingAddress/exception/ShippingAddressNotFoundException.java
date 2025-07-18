package com.promesa.promesa.domain.shippingAddress.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class ShippingAddressNotFoundException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new ShippingAddressNotFoundException();

    private ShippingAddressNotFoundException() {
        super(ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND);
    }
}
