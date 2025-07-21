package com.promesa.promesa.domain.order.exception;

import com.promesa.promesa.common.dto.ErrorReason;
import com.promesa.promesa.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.promesa.promesa.common.consts.PromesaStatic.*;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements BaseErrorCode {
    ORDER_NOT_FOUND(NOT_FOUND, "ORDER_404", "해당 주문을 찾을 수 없습니다."),
    INVALID_QUANTITY(BAD_REQUEST, "ORDER_400_1", "주문 수량은 1 이상이어야 합니다.");

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
