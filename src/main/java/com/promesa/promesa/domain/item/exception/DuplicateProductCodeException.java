package com.promesa.promesa.domain.item.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class DuplicateProductCodeException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new DuplicateProductCodeException();

    private DuplicateProductCodeException() {
        super(ItemErrorCode.INSUFFICIENT_STOCK);
    }
}
