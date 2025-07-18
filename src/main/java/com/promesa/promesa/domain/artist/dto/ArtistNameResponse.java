package com.promesa.promesa.domain.artist.dto;

import com.promesa.promesa.domain.artist.domain.Artist;

public record ArtistNameResponse(
        Long id,
        String name,
        String englishName,
        String artistPageUrl
) {
    public static ArtistNameResponse from(Artist artist) {
        return new ArtistNameResponse(
                artist.getId(),
                artist.getName(),
                "", // artist.getEnglishName() -> 아직 컬럼 없어서 빈값
                "/artists/" + artist.getId()
        );
    }
}
