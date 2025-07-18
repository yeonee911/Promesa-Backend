package com.promesa.promesa.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderItemRequest(
        @Schema(description = "type이 'SINGLE'이면 itemId, 'CART'이면 cartId (장바구니 ID)")
        Long itemId,
        @Schema(description = "'SINGLE'일 경우에만 사용됨. 'CART'일 경우 무시됨")
        int quantity
) {}
