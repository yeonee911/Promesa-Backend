package com.promesa.promesa.domain.item.dto.response;

import com.promesa.promesa.domain.item.domain.Item;

public record ItemDetail(
        String productCode,
        String type,
        int width,
        int height,
        int depth
) {
    public static ItemDetail from(Item item, String categoryName) {
        return new ItemDetail(
                item.getProductCode(),
                categoryName,
                item.getWidth(),
                item.getHeight(),
                item.getDepth()
        );
    }
}

