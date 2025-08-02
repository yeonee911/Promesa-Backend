package com.promesa.promesa.domain.exhibition.exception;

import com.promesa.promesa.common.dto.ErrorReason;
import com.promesa.promesa.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.promesa.promesa.common.consts.PromesaStatic.*;

@Getter
@AllArgsConstructor
public enum ExhibitionErrorCode implements BaseErrorCode {
    EXHIBITION_NOT_FOUND(NOT_FOUND, "Exhibition_404", "해당 기획전을 찾을 수 없습니다."),
    DUPLICATE_EXHIBITION_TITLE(CONFLICT, "Exhibition_409", "이미 존재하는 제목입니다."),
    INVALID_EXHIBITION_DATE(BAD_REQUEST, "Exhibition_400", "유효하지 않은 날짜 형식입니다.");

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
