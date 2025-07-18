package com.promesa.promesa.domain.shippingAddress.exception;

import com.promesa.promesa.common.dto.ErrorReason;
import com.promesa.promesa.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import static com.promesa.promesa.common.consts.PromesaStatic.NOT_FOUND;
import static com.promesa.promesa.common.consts.PromesaStatic.CONFLICT;

@Getter
@AllArgsConstructor
public enum ShippingAddressErrorCode implements BaseErrorCode {

    SHIPPING_ADDRESS_DUPLICATE(CONFLICT, "ShippingAddress_409", "기본 배송지는 하나만 등록 가능합니다."),
    SHIPPING_ADDRESS_NOT_FOUND(NOT_FOUND, "ShippingAddress_404", "기본 배송지가 존재하지 않습니다.");

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
