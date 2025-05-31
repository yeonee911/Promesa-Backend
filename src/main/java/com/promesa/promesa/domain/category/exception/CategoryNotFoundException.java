package com.promesa.promesa.domain.category.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class CategoryNotFoundException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new CategoryNotFoundException();
    private CategoryNotFoundException() {
        super(CategoryErrorCode.CATEGORY_NOT_FOUND);
    }
}
