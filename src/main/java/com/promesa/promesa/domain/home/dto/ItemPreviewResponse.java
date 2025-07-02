package com.promesa.promesa.domain.home.dto;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.item.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record ItemPreviewResponse (
        Long itemId,
        String itemName,
        String itemDescription,
        int price,
        String imageUrl,
        String artistName,
        boolean isWished

) {
    public static ItemPreviewResponse of(ItemPreviewResponse response, String imageUrl) {
        return new ItemPreviewResponse(
                response.itemId(),
                response.itemName(),
                response.itemDescription(),
                response.price(),
                imageUrl,
                response.artistName(),
                response.isWished()
        );
    }
}