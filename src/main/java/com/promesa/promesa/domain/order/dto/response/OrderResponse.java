package com.promesa.promesa.domain.order.dto.response;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.delivery.domain.Delivery;
import com.promesa.promesa.domain.item.domain.ItemImage;
import com.promesa.promesa.domain.order.domain.Order;

import java.util.List;

public record OrderResponse(
        OrderSummary summary,
        PaymentInfo deposit,
        DeliveryInfo delivery,
        List<OrderItemDetail> items
) {
    public static OrderResponse of(Order order, Delivery delivery, S3Service s3Service, String bucketName) {
        // 대표 이미지 presigned URL 생성
        String orderThumbnail = order.getOrderItems().get(0).getItem().getItemImages().stream()
                .filter(ItemImage::isThumbnail)
                .map(img -> s3Service.createPresignedGetUrl(bucketName, img.getImageKey()))
                .findFirst()


                .orElse(null);

        List<OrderItemDetail> items = order.getOrderItems().stream()
                .map(orderItem -> {
                    String orderItemThumbnail = orderItem.getItem().getItemImages().stream()
                            .filter(ItemImage::isThumbnail)
                            .map(img -> s3Service.createPresignedGetUrl(bucketName, img.getImageKey()))
                            .findFirst()
                            .orElse(null);
                    return OrderItemDetail.from(orderItem, orderItemThumbnail);
                })
                .toList();

        return new OrderResponse(
                OrderSummary.from(order, orderThumbnail, delivery),
                PaymentInfo.from(order),
                DeliveryInfo.from(delivery),
                items
        );
    }
}
