package com.promesa.promesa.domain.exhibition.dto.response;

import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class ExhibitionResponse {
    ExhibitionSummary summary;
    List<ItemPreviewResponse> itemPreviews;

    private ExhibitionResponse(ExhibitionSummary summary, List<ItemPreviewResponse> itemPreviews) {
        this.summary = summary;
        this.itemPreviews = itemPreviews;
    }

    public static ExhibitionResponse of(ExhibitionSummary summary, List<ItemPreviewResponse> itemPreviews) {
        return new ExhibitionResponse(summary, itemPreviews);
    }
}
