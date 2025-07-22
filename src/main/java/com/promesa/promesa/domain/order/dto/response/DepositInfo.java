package com.promesa.promesa.domain.order.dto.response;

import com.promesa.promesa.domain.order.domain.Order;

import java.time.LocalDateTime;

public record DepositInfo(
        String bankName,
        String accountNumber,
        String depositorName,
        LocalDateTime depositDeadline
) {
    public static DepositInfo from(Order order) {
        return new DepositInfo(
                order.getBankName(),
                order.getAccountNumber(),
                order.getDepositorName(),
                order.getDepositDeadline()
        );
    }
}

