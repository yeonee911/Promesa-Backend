package com.promesa.promesa.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtException extends AuthenticationException {
    public static final InvalidJwtException EXCEPTION = new InvalidJwtException();

    private InvalidJwtException() {
        super(JwtErrorCode.INVALID_TOKEN.getReason());
    }

    public JwtErrorCode getErrorCode() {
        return JwtErrorCode.INVALID_TOKEN;
    }
}
