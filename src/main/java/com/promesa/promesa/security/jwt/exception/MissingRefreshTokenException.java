package com.promesa.promesa.security.jwt.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class MissingRefreshTokenException extends PromesaCodeException {
    public static final MissingRefreshTokenException EXCEPTION = new MissingRefreshTokenException();

    private MissingRefreshTokenException() {
        super(JwtErrorCode.MISSING_REFRESH_TOKEN);
    }
}
