package com.promesa.promesa.domain.exhibition.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class DuplicateExhibitionTitleException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new DuplicateExhibitionTitleException();
    private DuplicateExhibitionTitleException() {
        super(ExhibitionErrorCode.DUPLICATE_EXHIBITION_TITLE);
    }
}