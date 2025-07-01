package com.promesa.promesa.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class ExpiredJwtException extends AuthenticationException {
    public static final ExpiredJwtException EXCEPTION = new ExpiredJwtException();

    private ExpiredJwtException() {
        super(JwtErrorCode.EXPIRED_TOKEN.getReason());
    }

    public JwtErrorCode getErrorCode() {
        return JwtErrorCode.EXPIRED_TOKEN;
    }
}
