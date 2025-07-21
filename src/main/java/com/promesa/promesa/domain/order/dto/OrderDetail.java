package com.promesa.promesa.domain.order.dto;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.delivery.domain.Delivery;
import com.promesa.promesa.domain.item.domain.ItemImage;
import com.promesa.promesa.domain.order.domain.Order;

import java.util.List;

public record OrderDetail(
        OrderSummary summary,
        DepositInfo deposit,
        DeliveryInfo delivery,
        List<OrderItemDetail> items
) {
    public static OrderDetail of(Order order, Delivery delivery, S3Service s3Service, String bucketName) {
        // 대표 이미지 presigned URL 생성
        String thumbnailUrl = order.getOrderItems().get(0).getItem().getItemImages().stream()
                .filter(ItemImage::isThumbnail)
                .map(img -> s3Service.createPresignedGetUrl(bucketName, img.getImageKey()))
                .findFirst()
                .orElse(null);

        return new OrderDetail(
                OrderSummary.from(order, thumbnailUrl, delivery),
                DepositInfo.from(order),
                DeliveryInfo.from(delivery),
                order.getOrderItems().stream()
                        .map(OrderItemDetail::from)
                        .toList()
        );
    }
}
