package com.promesa.promesa.domain.example.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class ExampleNotFoundException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new ExampleNotFoundException();

    private ExampleNotFoundException() {
        super(ExampleErrorCode.EXAMPLE_ERROR_CODE);
    }
}
