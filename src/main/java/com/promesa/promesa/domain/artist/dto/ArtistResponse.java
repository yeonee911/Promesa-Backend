package com.promesa.promesa.domain.artist.dto;

import com.promesa.promesa.domain.artist.domain.Artist;

public record ArtistResponse(
        ArtistProfile profile,
        ArtistWish wish
){
    public static ArtistResponse from(Artist artist,String presignedUrl,boolean isWishlisted) {
        return new ArtistResponse(
                new ArtistProfile(
                        artist.getId(),
                        artist.getName(),
                        presignedUrl,
                        artist.getDescription(),
                        "https://instagram.com/" + artist.getInsta()
                ),
                new ArtistWish(
                        isWishlisted,
                        artist.getWishCount()
                )
        );
    }
}
