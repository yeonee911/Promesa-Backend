package com.promesa.promesa.security.jwt.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class InvalidRefreshTokenException extends PromesaCodeException {
    public static final InvalidRefreshTokenException EXCEPTION = new InvalidRefreshTokenException();

    private InvalidRefreshTokenException() {
        super(JwtErrorCode.INVALID_REFRESH_TOKEN);
    }
}

