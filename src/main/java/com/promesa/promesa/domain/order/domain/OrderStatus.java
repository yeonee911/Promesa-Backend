package com.promesa.promesa.domain.order.domain;

public enum OrderStatus {
    WAITING_FOR_PAYMENT,   // 주문했지만 아직 결제 안 됨
    PAID,                  // 입금 확인
    CANCEL,                // 사용자가 직접 취소
    CANCEL_NO_PAYMENT      // 미입금 취소
}