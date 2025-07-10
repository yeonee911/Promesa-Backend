package com.promesa.promesa.domain.item.dto;

import com.promesa.promesa.domain.artist.dto.ArtistProfile;
import com.promesa.promesa.domain.artist.dto.ArtistWish;

public record ItemResponse(
        ItemSummary itemSummary,
        ItemDetail itemDetail,
        ItemWish itemWish,
        ArtistProfile artistProfile,
        ArtistWish artistWish,
        ItemSale itemSales
) {}
