package com.promesa.promesa.domain.wish.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class DuplicatedWishException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new DuplicatedWishException();

    private DuplicatedWishException() {
        super(WishErrorCode.DUPLICATED_WISH);
    }
}
