package com.promesa.promesa.domain.exhibition.dto.response;

import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ExhibitionDetailResponse {
    ExhibitionSummary summary;
    ExhibitionDetail detail;
    List<ItemPreviewResponse> itemPreviews;

    private ExhibitionDetailResponse(ExhibitionSummary summary, ExhibitionDetail detail, List<ItemPreviewResponse> itemPreviews) {
        this.summary = summary;
        this.detail = detail;
        this.itemPreviews = itemPreviews;
    }

    public static ExhibitionDetailResponse of(ExhibitionSummary summary, ExhibitionDetail detail, List<ItemPreviewResponse> itemPreviews) {
        return new ExhibitionDetailResponse(summary, detail, itemPreviews);
    }
}
