package com.promesa.promesa.domain.artist.dto;

import com.promesa.promesa.domain.artist.domain.Artist;

public record ArtistResponse(
        ArtistProfile profile,
        ArtistWish wish
){
    public static ArtistResponse of(Artist artist, String presignedUrl, boolean isWishlisted) {
        return new ArtistResponse(
                ArtistProfile.from(artist, presignedUrl),
                ArtistWish.from(artist, isWishlisted)
        );
    }
}
