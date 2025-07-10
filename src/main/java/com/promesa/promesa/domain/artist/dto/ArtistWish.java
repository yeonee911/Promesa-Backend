package com.promesa.promesa.domain.artist.dto;

public record ArtistWish(
        boolean isWishlisted,
        int wishCount
) {}
