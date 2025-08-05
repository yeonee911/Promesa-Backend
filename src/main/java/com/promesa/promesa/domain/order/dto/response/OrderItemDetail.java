package com.promesa.promesa.domain.order.dto.response;

import com.promesa.promesa.domain.order.domain.OrderItem;
import com.promesa.promesa.domain.order.domain.OrderItemStatus;

public record OrderItemDetail(
        Long orderItemId,
        Long itemId,
        String itemName,
        String orderItemThumbnail,
        int price,
        int quantity,
        OrderItemStatus itemStatus
) {
    public static OrderItemDetail from(OrderItem item, String orderThumbnail) {
        return new OrderItemDetail(
                item.getId(),
                item.getItem().getId(),
                item.getItem().getName(),
                orderThumbnail,
                item.getPrice(),
                item.getQuantity(),
                item.getOrderItemStatus()
        );
    }
}
