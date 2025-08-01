package com.promesa.promesa.domain.item.dto.response;

import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.domain.ItemStatus;

public record ItemSale(
        int stock,
        ItemStatus saleStatus,
        int price,
        boolean freeShipping,
        String shippingPolicy
) {
    public static ItemSale from(Item item) {
        return new ItemSale(
                item.getStock(),
                item.getItemStatus(),
                item.getPrice(),
                item.getPrice() >= 70000,
                "제주/도서산간 3,000원 추가"
        );
    }
}


