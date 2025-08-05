package com.promesa.promesa.domain.review.dto.response;

import com.promesa.promesa.domain.order.dto.response.OrderItemSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewDetailQueryDto {
    private final OrderItemSummary orderItemSummary;
    private final ReviewQueryDto review;
}
