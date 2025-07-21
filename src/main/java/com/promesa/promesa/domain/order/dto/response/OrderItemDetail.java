package com.promesa.promesa.domain.order.dto.response;

import com.promesa.promesa.domain.order.domain.OrderItem;

public record OrderItemDetail(
        Long itemId,
        String itemName,
        int price,
        int quantity
) {
    public static OrderItemDetail from(OrderItem item) {
        return new OrderItemDetail(
                item.getItem().getId(),
                item.getItem().getName(),
                item.getPrice(),
                item.getQuantity()
        );
    }
}
