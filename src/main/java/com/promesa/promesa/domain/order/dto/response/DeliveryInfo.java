package com.promesa.promesa.domain.order.dto.response;

import com.promesa.promesa.domain.delivery.domain.Delivery;
import com.promesa.promesa.domain.delivery.domain.DeliveryStatus;

import java.time.LocalDate;

public record DeliveryInfo(
        String receiverName,
        String receiverPhone,
        String zipCode,
        String address,
        String addressDetail,
        int deliveryFee,
        LocalDate deliveryExpectedDate, // 배송 예정일
        LocalDate deliveryStartDate, // 배송 시작일
        LocalDate deliveryCompletedDate, // 배송 완료일
        DeliveryStatus deliveryStatus // 배송 상태
) {
    public static DeliveryInfo from(Delivery delivery) {
        return new DeliveryInfo(
                delivery.getReceiverName(),
                delivery.getReceiverPhone(),
                delivery.getZipCode(),
                delivery.getAddress(),
                delivery.getAddressDetail(),
                delivery.getDeliveryFee(),
                delivery.getDeliveryExpectedDate(),
                delivery.getDeliveryStartDate(),
                delivery.getDeliveryCompletedDate(),
                delivery.getDeliveryStatus()
        );
    }
}
