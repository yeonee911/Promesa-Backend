package com.promesa.promesa.domain.exhibition.dto.response;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.exhibition.domain.Exhibition;
import lombok.Getter;

@Getter
public class ExhibitionDetail {
    private String detailedImageKey;
    private String detailedImageUrl;

    private ExhibitionDetail(String detailedImageKey, String detailedImageUrl) {
        this.detailedImageKey = detailedImageKey;
        this.detailedImageUrl = detailedImageUrl;
    }

    public static ExhibitionDetail of(Exhibition exhibition, String imageUrl) {
        return new ExhibitionDetail(
                exhibition.getDetailedImageKey(),
                imageUrl
        );
    }
}
