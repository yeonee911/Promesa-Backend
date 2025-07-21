package com.promesa.promesa.domain.order.dto.response;

import com.promesa.promesa.domain.delivery.domain.Delivery;
import com.promesa.promesa.domain.order.domain.Order;
import com.promesa.promesa.domain.order.domain.OrderStatus;

import java.time.LocalDateTime;

public record OrderSummary(
        Long orderId,
        LocalDateTime orderDate, // 주문 날짜
        OrderStatus orderStatus, // 주문 상태
        int totalAmount, // 총 결제 금액
        int totalQuantity, // 주문 작품 총 수량
        String itemThumbnail, // 대표 이미지 URL
        String itemName,      // 대표 상품명
        String buyerName,     // 구매자 이름
        String buyerPhone,    // 구매자 연락처
        LocalDateTime deliveryExpectedDate, // 배송 예정일
        LocalDateTime deliveryStartDate, // 배송 시작일
        LocalDateTime deliveryCompletedDate // 배송 완료일
) {
    public static OrderSummary from(Order order, String itemThumbnailUrl, Delivery delivery) {
        return new OrderSummary(
                order.getId(),
                order.getCreatedAt(),
                order.getOrderStatus(),
                order.getTotalAmount(),
                order.getTotalQuantity(),
                itemThumbnailUrl,
                order.getOrderItems().get(0).getItem().getName(),
                order.getMember().getName(),
                order.getMember().getPhone(),
                delivery.getDeliveryExpectedDate(),
                delivery.getDeliveryStartDate(),
                delivery.getDeliveryCompletedDate()
        );
    }
}


