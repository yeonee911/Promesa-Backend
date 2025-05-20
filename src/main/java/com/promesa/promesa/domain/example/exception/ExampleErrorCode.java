package com.promesa.promesa.domain.example.exception;

import com.promesa.promesa.common.dto.ErrorReason;
import com.promesa.promesa.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.promesa.promesa.common.consts.PromesaStatic.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ExampleErrorCode implements BaseErrorCode {
    EXAMPLE_ERROR_CODE(NOT_FOUND, "Example_404", "예시를 찾을 수 없습니다.");

    private Integer status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder()
                .reason(reason)
                .code(code)
                .status(status)
                .build();
    }
}
