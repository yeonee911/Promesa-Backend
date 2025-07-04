package com.promesa.promesa.security.jwt.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class ExpiredRefreshTokenException extends PromesaCodeException {
    public static final ExpiredRefreshTokenException EXCEPTION = new ExpiredRefreshTokenException();

    private ExpiredRefreshTokenException() {
        super(JwtErrorCode.EXPIRED_REFRESH_TOKEN);
    }
}
