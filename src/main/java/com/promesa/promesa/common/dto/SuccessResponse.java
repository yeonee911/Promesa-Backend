package com.promesa.promesa.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessResponse<T> {
    private final boolean success = true;
    private int status;       // HTTP status code
    private final LocalDateTime timeStamp = LocalDateTime.now();
    private T data;         // 응답 데이터

    public static <T> SuccessResponse<T> success(int code, T data) {
        return new SuccessResponse<T>(code, data);
    }
}
