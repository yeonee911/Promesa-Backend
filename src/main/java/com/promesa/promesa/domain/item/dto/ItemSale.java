package com.promesa.promesa.domain.item.dto;

import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.domain.SaleStatus;

public record ItemSale(
        int stock,
        SaleStatus saleStatus,
        int price,
        boolean freeShipping,
        String shippingPolicy
) {
    public static ItemSale from(Item item) {
        return new ItemSale(
                item.getStock(),
                item.getSaleStatus(),
                item.getPrice(),
                item.getPrice() >= 70000,
                "제주/도서산간 3,000원 추가"
        );
    }
}


