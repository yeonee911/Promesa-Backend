package com.promesa.promesa.domain.delivery.dto.response;

import com.promesa.promesa.domain.delivery.domain.Delivery;
import com.promesa.promesa.domain.delivery.domain.DeliveryStatus;

import java.time.LocalDate;

public record DeliveryResponse(
        Long deliveryId,
        String courierName,
        String receiverName,
        String receiverPhone,
        String zipCode,
        String address,
        String addressDetail,
        DeliveryStatus deliveryStatus,
        LocalDate deliveryExpectedDate,
        LocalDate deliveryStartDate,
        LocalDate deliveryCompletedDate,
        int deliveryFee,
        Long orderId
) {
    public static DeliveryResponse from(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getCourierName(),
                delivery.getReceiverName(),
                delivery.getReceiverPhone(),
                delivery.getZipCode(),
                delivery.getAddress(),
                delivery.getAddressDetail(),
                delivery.getDeliveryStatus(),
                delivery.getDeliveryExpectedDate(),
                delivery.getDeliveryStartDate(),
                delivery.getDeliveryCompletedDate(),
                delivery.getDeliveryFee(),
                delivery.getOrder() != null ? delivery.getOrder().getId() : null
        );
    }
}

