package com.promesa.promesa.domain.item.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class InsufficientStockException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new InsufficientStockException();

    private InsufficientStockException() {
        super(ItemErrorCode.INSUFFICIENT_STOCK);
    }
}
