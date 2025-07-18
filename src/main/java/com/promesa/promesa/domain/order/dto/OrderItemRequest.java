package com.promesa.promesa.domain.order.dto;

public record OrderItemRequest(
        Long itemId,   // type이 "SINGLE"이면 itemId, "CART"이면 cartId
        int quantity   // (선택) CART는 무시해도 됨
) {}
