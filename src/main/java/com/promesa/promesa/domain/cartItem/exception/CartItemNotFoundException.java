package com.promesa.promesa.domain.cartItem.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class CartItemNotFoundException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new CartItemNotFoundException();

    private CartItemNotFoundException() {
        super(CartItemErrorCode.CART_ITEM_NOT_FOUND);
    }
}

