package com.promesa.promesa.domain.order.dto.response;

import com.promesa.promesa.domain.order.domain.Order;

import java.time.LocalDateTime;

public record PaymentInfo(
        String bankName,
        String depositorName,
        LocalDateTime depositDeadline
) {
    public static PaymentInfo from(Order order) {
        return new PaymentInfo(
                order.getBankName(),
                order.getDepositorName(),
                order.getDepositDeadline()
        );
    }
}

