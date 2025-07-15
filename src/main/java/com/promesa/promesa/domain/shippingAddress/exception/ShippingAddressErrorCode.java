package com.promesa.promesa.domain.shippingAddress.exception;

import com.promesa.promesa.common.dto.ErrorReason;
import com.promesa.promesa.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.promesa.promesa.common.consts.PromesaStatic.CONFLICT;

@Getter
@AllArgsConstructor
public enum ShippingAddressErrorCode implements BaseErrorCode {

    SHIPPING_ADDRESS_DUPLICATE(CONFLICT, "ShippingAddress_409", "기본 배송지는 하나만 등록 가능합니다.");

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
