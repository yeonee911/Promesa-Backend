package com.promesa.promesa.domain.artist.dto.request;

import lombok.Getter;

@Getter
public class UpdateArtistInfoRequest {
    private String artistName;
    private String subName;
    private String description;
    private String insta;
}
