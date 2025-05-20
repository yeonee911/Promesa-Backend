package com.promesa.promesa.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private final boolean success = false;
    private int status;   // 기본형 : 항상 값이 존재한느 필드
    private String code;
    private  String reason;
    private final LocalDateTime timeStamp = LocalDateTime.now();
    private String path;

    public static ErrorResponse of(int status, String code, String reason, String path) {
        return new ErrorResponse(status, code, reason, path);
    }

    public static ErrorResponse from(ErrorReason errorReason, String path) {
        return new ErrorResponse(
                errorReason.getStatus(),
                errorReason.getCode(),
                errorReason.getReason(),
                path
        );
    }
}