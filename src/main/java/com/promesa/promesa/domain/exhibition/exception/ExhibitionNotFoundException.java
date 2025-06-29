package com.promesa.promesa.domain.exhibition.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class ExhibitionNotFoundException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new ExhibitionNotFoundException();
    private ExhibitionNotFoundException() {
        super(ExhibitionErrorCode.EXHIBITION_NOT_FOUND);
    }
}