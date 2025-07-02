package com.promesa.promesa.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPreviewResponse {
    private Long itemId;
    private String itemName;
    private String itemDescription;
    private int price;
    private String imageUrl;
    private String artistName;
    private boolean isWished;

    public static ItemPreviewResponse of(ItemPreviewResponse response, String imageUrl) {
        return new ItemPreviewResponse(
                response.getItemId(),
                response.getItemName(),
                response.getItemDescription(),
                response.getPrice(),
                imageUrl,
                response.getArtistName(),
                response.isWished()
        );
    }
}