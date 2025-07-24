package com.promesa.promesa.domain.order.dto.request;

public record PaymentRequest(
        String paymentMethod,     // 무통장입금 등
        String bankName,          // ex) "신한은행 123-456-789098"
        String depositorName      // 사용자가 입력한 입금자명
) {}


