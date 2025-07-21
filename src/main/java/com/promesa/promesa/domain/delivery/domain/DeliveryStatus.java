package com.promesa.promesa.domain.delivery.domain;

public enum DeliveryStatus {
    READY,       // 배송 준비 중
    SHIPPED,     // 배송 출발
    DELIVERED,   // 배송 완료
    RETURNING,   // 반품 회수 중
    EXCHANGING,  // 교환 배송 중
    RETURNED,    // 반품 배송 완료
    EXCHANGED    // 교환 배송 완료
}

