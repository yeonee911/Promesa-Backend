package com.promesa.promesa.domain.review.dto.response;

import com.promesa.promesa.domain.order.dto.response.OrderItemSummary;
import lombok.Getter;

@Getter
public class ReviewDetailResponse {
    OrderItemSummary orderItemSummary;
    ReviewResponse reviewResponse;

    public ReviewDetailResponse(OrderItemSummary orderItemSummary, ReviewResponse reviewResponse) {
        this.orderItemSummary = orderItemSummary;
        this.reviewResponse = reviewResponse;
    }

    public static ReviewDetailResponse of(OrderItemSummary orderItemSummary, ReviewResponse reviewResponse) {
        return new ReviewDetailResponse(orderItemSummary, reviewResponse);
    }
}
