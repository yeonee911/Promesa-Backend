package com.promesa.promesa.domain.item.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class InvalidSaleStatusChangeException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new InvalidSaleStatusChangeException();

    private InvalidSaleStatusChangeException() {
        super(ItemErrorCode.INVALID_SALE_STATUS_CHANGE);
    }
}
