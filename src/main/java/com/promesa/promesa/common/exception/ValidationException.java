package com.promesa.promesa.common.exception;

public class ValidationException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new ValidationException();

    private ValidationException() {
        super(GlobalErrorCode.VALIDATION_EXCEPTION);
    }
}
