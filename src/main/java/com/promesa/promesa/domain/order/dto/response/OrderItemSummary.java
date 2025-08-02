package com.promesa.promesa.domain.order.dto.response;

import com.promesa.promesa.domain.delivery.domain.DeliveryStatus;
import com.promesa.promesa.domain.order.domain.OrderItem;
import com.promesa.promesa.domain.order.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderItemSummary {
    private Long orderId;
    private Long orderItemId;
    private Long itemId;
    private String itemName;
    private String artistName;
    private String itemThumbnail;
    private LocalDateTime orderDate;
    private DeliveryStatus deliveryStatus;
    private int quantity;

    public void setItemThumbnail(String itemThumbnail) {
        this.itemThumbnail = itemThumbnail;
    }
}
