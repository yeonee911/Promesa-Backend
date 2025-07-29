package com.promesa.promesa.domain.review.exception;

import com.promesa.promesa.common.dto.ErrorReason;
import com.promesa.promesa.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.promesa.promesa.common.consts.PromesaStatic.*;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {
    REVIEW_DUPLICATE(CONFLICT, "Review_409", "gg"),
    REVIEW_NOT_FOUND(NOT_FOUND, "Review_404", "해당 리뷰를 찾을 수 없습니다."),
    REVIEW_ACCESS_DENIED(FORBIDDEN, "Reivew_403", "해당 리뷰에 대한 권한이 없습니다."),
    REVIEW_ITEM_MISMATCH(BAD_REQUEST, "Reivew_400", "리뷰와 상품이 일치하지 않습니다."),
    REVIEW_UNAUTHORIZED(UNAUTHORIZED, "REVIEW_401", "로그인이 필요합니다."),
    REVIEW_ORDER_ITEM_FORBIDDEN(FORBIDDEN, "REVIEW_403", "본인의 주문이 아니거나 해당 작품에 대한 주문이 아닙니다."),
    REVIEW_ITEM_NOT_DELIVERED(FORBIDDEN, "REVIEW_403", "상품이 배송되지 않아 리뷰를 작성할 수 없습니다."),
    ;

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
