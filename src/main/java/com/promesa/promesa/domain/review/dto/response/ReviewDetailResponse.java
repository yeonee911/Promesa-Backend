package com.promesa.promesa.domain.review.dto.response;

import com.promesa.promesa.domain.order.dto.response.OrderItemSummary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
