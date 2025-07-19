package com.promesa.promesa.domain.home.dto.response;

import com.promesa.promesa.domain.artist.dto.ArtistResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResponse {
    ArtistResponse artist;
    ItemPreviewResponse itemPreview;
}
