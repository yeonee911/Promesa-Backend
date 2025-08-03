package com.promesa.promesa.domain.order.domain;

public enum OrderItemStatus {
    CANCEL_REQUESTED,      // 취소 접수
    CANCELLED,             // 취소 완료
    RETURN_REQUESTED,      // 반품 접수
    RETURNED,              // 반품 완료
    EXCHANGE_REQUESTED,    // 교환 접수
    EXCHANGED,             // 교환 완료
}
