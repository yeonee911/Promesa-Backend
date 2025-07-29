package com.promesa.promesa.domain.exhibition.dto.response;

import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ExhibitionDetailResponse {
    ExhibitionSummary summary;
    private String DetailedImageUrl;
    List<ItemPreviewResponse> itemPreviews;

    private ExhibitionDetailResponse(ExhibitionSummary summary, String detailedImageUrl, List<ItemPreviewResponse> itemPreviews) {
        this.summary = summary;
        this.DetailedImageUrl = detailedImageUrl;
        this.itemPreviews = itemPreviews;
    }

    public static ExhibitionDetailResponse of(ExhibitionSummary summary, String detailedImageUrl, List<ItemPreviewResponse> itemPreviews) {
        return new ExhibitionDetailResponse(summary, detailedImageUrl, itemPreviews);
    }
}
