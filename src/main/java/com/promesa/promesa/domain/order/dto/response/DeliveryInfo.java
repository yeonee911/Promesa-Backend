package com.promesa.promesa.domain.order.dto.response;

import com.promesa.promesa.domain.delivery.domain.Delivery;

public record DeliveryInfo(
        String receiverName,
        String receiverPhone,
        String zipCode,
        String address,
        String addressDetail
) {
    public static DeliveryInfo from(Delivery delivery) {
        return new DeliveryInfo(
                delivery.getReceiverName(),
                delivery.getReceiverPhone(),
                delivery.getZipCode(),
                delivery.getAddress(),
                delivery.getAddressDetail()
        );
    }
}
