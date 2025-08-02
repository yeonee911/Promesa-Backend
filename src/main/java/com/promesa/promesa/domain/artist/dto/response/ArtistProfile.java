package com.promesa.promesa.domain.artist.dto.response;

import com.promesa.promesa.domain.artist.domain.Artist;

public record ArtistProfile(
        Long artistId,
        String name,
        String subname,
        String profileImageUrl,
        String bio,
        String instagramUrl
) {
    public static ArtistProfile from(Artist artist, String presignedUrl) {
        return new ArtistProfile(
                artist.getId(),
                artist.getName(),
                artist.getSubname(),
                presignedUrl,
                artist.getDescription(),
                "https://www.instagram.com/" + artist.getInsta()
        );
    }
}

