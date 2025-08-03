package com.promesa.promesa.domain.artist.dto.request;

import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class UpdateArtistInfoRequest {
    private JsonNullable<String> artistName = JsonNullable.undefined();
    private JsonNullable<String> subName = JsonNullable.undefined();
    private JsonNullable<String> description = JsonNullable.undefined();
    private JsonNullable<String> insta = JsonNullable.undefined();
}
