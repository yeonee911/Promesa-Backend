package com.promesa.promesa.security.jwt.exception;

import com.promesa.promesa.common.dto.ErrorReason;
import com.promesa.promesa.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.promesa.promesa.common.consts.PromesaStatic.*;

@Getter
@AllArgsConstructor
public enum JwtErrorCode implements BaseErrorCode {
    // Access Token 관련
    INVALID_TOKEN_FORMAT(BAD_REQUEST, "JWT_400", "잘못된 토큰 형식입니다."),
    INVALID_TOKEN(UNAUTHORIZED, "JWT_401", "유효하지 않은 Access Token입니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "JWT_402", "만료된 Access Token입니다."),

    // Refresh Token 관련
    MISSING_REFRESH_TOKEN(UNAUTHORIZED, "JWT_410", "Refresh Token이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "JWT_411", "유효하지 않은 Refresh Token입니다."),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "JWT_412", "만료된 Refresh Token입니다.");


    private final Integer status;
    private final String code;
    private final String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder()
                .status(status)
                .code(code)
                .reason(reason)
                .build();
    }
}
