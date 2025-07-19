package com.promesa.promesa.domain.order.domain;

public enum OrderStatus {
    WAITING_FOR_PAYMENT,   // 주문했지만 아직 결제 안 됨
    PAID,                  // 입금 확인
    SHIPPING,              // 배송 중
    DELIVERED,             // 배송 완료
    CANCEL_REQUESTED,      // 취소 접수
    CANCELLED,             // 취소 완료
    RETURN_REQUESTED,      // 반품 접수
    RETURNED,              // 반품 완료
    EXCHANGE_REQUESTED,    // 교환 접수
    EXCHANGED,             // 교환 완료
    CANCEL_BEFORE_PAYMENT, // 입금 전 취소
    CANCEL_NO_PAYMENT      // 미입금 자동 취소
}