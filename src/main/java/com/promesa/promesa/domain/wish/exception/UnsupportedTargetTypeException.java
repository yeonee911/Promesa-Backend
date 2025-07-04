package com.promesa.promesa.domain.wish.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class UnsupportedTargetTypeException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new UnsupportedTargetTypeException();

    private UnsupportedTargetTypeException() {
        super(WishErrorCode.UNSUPPORTED_TARGET_TYPE);
    }
}