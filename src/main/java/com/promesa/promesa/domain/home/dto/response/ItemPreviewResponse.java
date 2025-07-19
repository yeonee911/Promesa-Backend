package com.promesa.promesa.domain.home.dto.response;

import com.promesa.promesa.domain.item.domain.SaleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPreviewResponse {
    private Long itemId;
    private SaleStatus saleStatus;
    private String itemName;
    private int price;
    private String imageUrl;
    private String artistName;
    private boolean isWished;
    private int wishCount;

    public static ItemPreviewResponse of(ItemPreviewResponse response, String imageUrl) {
        return new ItemPreviewResponse(
                response.getItemId(),
                response.getItemName(),
                response.getItemDescription(),
                response.getPrice(),
                imageUrl,
                response.getArtistName(),
                response.isWished(),
                response.getWishCount()
        );
    }
}
