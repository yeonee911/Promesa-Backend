package com.promesa.promesa.domain.cart.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class CartNotFoundException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new CartNotFoundException();

    private CartNotFoundException() {
        super(CartErrorCode.CART_NOT_FOUND);
    }
}

