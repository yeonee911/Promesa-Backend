package com.promesa.promesa.domain.artist.dto;

public record ArtistProfile(
        Long artistId,
        String name,
        String profileImageUrl,
        String bio,
        String instagramUrl
) {}

