package com.promesa.promesa.domain.item.dto.response;

import com.promesa.promesa.domain.item.domain.Item;

public record ItemWish(
        boolean isWishlisted,
        int wishCount
) {
    public static ItemWish from(Item item, boolean isWishlisted) {
        return new ItemWish(
                isWishlisted,
                item.getWishCount()
        );
    }
}
