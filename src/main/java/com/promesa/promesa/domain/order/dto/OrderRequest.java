package com.promesa.promesa.domain.order.dto;

import jakarta.validation.Valid;

import java.util.List;

public record OrderRequest(
        String type,                       // "SINGLE" 또는 "CART"
        @Valid List<OrderItemRequest> items      // 주문 항목 목록
) {}
