package com.promesa.promesa.domain.item.dto;

import com.promesa.promesa.domain.item.domain.Item;

public record ItemSale(
        int stock,
        boolean soldOut,
        int price,
        boolean freeShipping,
        String shippingPolicy
) {
    public static ItemSale from(Item item) {
        return new ItemSale(
                item.getStock(),
                item.getStock() == 0,
                item.getPrice(),
                item.getPrice() >= 70000,
                "제주/도서산간 3,000원 추가"
        );
    }
}


