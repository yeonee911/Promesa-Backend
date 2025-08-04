package com.promesa.promesa.common.exception;

import com.promesa.promesa.domain.example.exception.ExampleErrorCode;

public class InternalServerError extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new InternalServerError();
    private InternalServerError() {
        super(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }
}