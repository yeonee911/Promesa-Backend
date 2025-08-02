package com.promesa.promesa.domain.delivery.dto.request;

import com.promesa.promesa.domain.delivery.domain.DeliveryStatus;

import java.time.LocalDate;

public record DeliveryRequest(
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
        int deliveryFee
) {}
