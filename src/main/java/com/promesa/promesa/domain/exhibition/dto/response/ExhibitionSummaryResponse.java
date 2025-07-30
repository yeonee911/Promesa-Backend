package com.promesa.promesa.domain.exhibition.dto.response;

import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class ExhibitionSummaryResponse {
    ExhibitionSummary summary;
    List<ItemPreviewResponse> itemPreviews;

    private ExhibitionSummaryResponse(ExhibitionSummary summary, List<ItemPreviewResponse> itemPreviews) {
        this.summary = summary;
        this.itemPreviews = itemPreviews;
    }

    public static ExhibitionSummaryResponse of(ExhibitionSummary summary, List<ItemPreviewResponse> itemPreviews) {
        return new ExhibitionSummaryResponse(summary, itemPreviews);
    }
}
