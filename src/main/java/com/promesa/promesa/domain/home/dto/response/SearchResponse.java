package com.promesa.promesa.domain.home.dto.response;

import com.promesa.promesa.domain.artist.dto.response.ArtistResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchResponse {
    List<ArtistResponse> artists;
    List<ItemPreviewResponse> itemPreviews;
}
