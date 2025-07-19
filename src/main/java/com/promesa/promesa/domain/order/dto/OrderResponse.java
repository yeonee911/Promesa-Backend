package com.promesa.promesa.domain.order.dto;

public record OrderResponse(Long orderId, int totalPrice) {

    public static OrderResponse of(Long orderId, int totalPrice) {
        return new OrderResponse(orderId, totalPrice);
    }
}
