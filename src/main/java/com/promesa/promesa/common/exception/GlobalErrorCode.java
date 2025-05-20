package com.promesa.promesa.common.exception;

import com.promesa.promesa.common.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.promesa.promesa.common.consts.PromesaStatic.INTERNAL_SERVER;
import static com.promesa.promesa.common.consts.PromesaStatic.NOT_FOUND;

/**
 * 글로벌 관련 Exception Code를 모아뒀습니다.
 * 도메인 관련 Exception Code은 도메인 내부 exception 패키지에 작성하면 됩니다!
 */
@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
    EXAMPLE_NOT_FOUND(NOT_FOUND, "EXAMPLE_404", "예시를 찾을 수 없습니다."),

    INTERNAL_SERVER_ERROR(INTERNAL_SERVER, "GLOBAL_500", "서버 오류");

    private Integer status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder()
                .status(status)
                .code(code)
                .reason(reason)
                .build();
    }
}
