package com.promesa.promesa.domain.item.dto;

public record ItemSale(
        int stockQuantity,
        boolean isSoldOut,
        int Price,
        boolean isFreeShipping,
        String shippingPolicyNote
) {}

