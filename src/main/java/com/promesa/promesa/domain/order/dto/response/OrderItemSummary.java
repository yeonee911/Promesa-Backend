package com.promesa.promesa.domain.order.dto.response;

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
    private OrderStatus orderStatus;
    private int quantity;
}
