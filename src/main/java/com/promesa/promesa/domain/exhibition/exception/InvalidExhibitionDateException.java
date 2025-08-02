package com.promesa.promesa.domain.exhibition.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class InvalidExhibitionDateException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new InvalidExhibitionDateException();
    private InvalidExhibitionDateException() {
        super(ExhibitionErrorCode.DUPLICATE_EXHIBITION_TITLE);
    }
}