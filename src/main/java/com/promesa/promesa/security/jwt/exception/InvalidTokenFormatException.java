package com.promesa.promesa.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenFormatException extends AuthenticationException {
    public static final InvalidTokenFormatException EXCEPTION = new InvalidTokenFormatException();

    private InvalidTokenFormatException() {
        super(JwtErrorCode.INVALID_TOKEN_FORMAT.getReason());
    }

    public JwtErrorCode getErrorCode() {
        return JwtErrorCode.INVALID_TOKEN_FORMAT;
    }
}
