package com.promesa.promesa.domain.item.dto.response;

import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.dto.response.ArtistProfile;
import com.promesa.promesa.domain.artist.dto.response.ArtistWish;
import com.promesa.promesa.domain.category.domain.Category;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.domain.ItemImage;

import java.util.List;

// DTO 조립
public record ItemResponse(
        ItemSummary itemSummary,
        ItemDetail itemDetail,
        ItemWish itemWish,
        ArtistProfile artistProfile,
        ArtistWish artistWish,
        ItemSale itemSales
) {
    public static ItemResponse of(
            Item item,
            Category category,
            List<ItemImageResponse> mainImageUrls,
            List<ItemImageResponse> detailImageUrls,
            Artist artist,
            String artistImageUrl,
            boolean itemWished,
            boolean artistWished
    ) {
        return new ItemResponse(
                ItemSummary.from(item, category, mainImageUrls, detailImageUrls, artist),
                ItemDetail.from(item, category.getName()),
                ItemWish.from(item, itemWished),
                ArtistProfile.from(artist, artistImageUrl),
                ArtistWish.from(artist, artistWished),
                ItemSale.from(item)
        );
    }
}

