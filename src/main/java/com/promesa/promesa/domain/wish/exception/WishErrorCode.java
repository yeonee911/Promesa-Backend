package com.promesa.promesa.domain.wish.exception;

import com.promesa.promesa.common.dto.ErrorReason;
import com.promesa.promesa.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WishErrorCode implements BaseErrorCode {
    UNSUPPORTED_TARGET_TYPE(400, "WISH_400", "지원하지 않는 대상 타입입니다."),
    DUPLICATED_WISH(400, "WISH_401", "이미 위시리스트에 추가된 항목입니다.");

    private final Integer status;
    private final String code;
    private final String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder()
                .reason(reason)
                .code(code)
                .status(status)
                .build();
    }
}
