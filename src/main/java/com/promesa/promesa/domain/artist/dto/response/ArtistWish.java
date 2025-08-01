package com.promesa.promesa.domain.artist.dto.response;

import com.promesa.promesa.domain.artist.domain.Artist;

public record ArtistWish(
        boolean isWishlisted,
        int wishCount
) {
    public static ArtistWish from(Artist artist, boolean isWishlisted) {
        return new ArtistWish(
                isWishlisted,
                artist.getWishCount()
        );
    }
}
