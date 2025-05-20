package com.promesa.promesa.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorReason {
    private final Integer status;   // 상태 코드
    private final String code;      // 도메인 + 상태코드
    private final String reason;    // 구체적 설명
}