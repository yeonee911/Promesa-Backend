package com.promesa.promesa.domain.artist.dto;

import com.promesa.promesa.domain.artist.domain.Artist;

public record ArtistResponse(
        String name,
        String profileImageUrl,
        String bio,
        String instagramUrl,
        int wishCount,
        boolean isWishlisted
){
    public static ArtistResponse from(Artist artist,boolean isWishlisted) {
        return new ArtistResponse(
                artist.getName(),
                artist.getProfileImageUrl(),
                artist.getDescription(),
                "https://instagram.com/" + artist.getInsta(),
                artist.getWishCount(),
                isWishlisted
        );
    }
}
