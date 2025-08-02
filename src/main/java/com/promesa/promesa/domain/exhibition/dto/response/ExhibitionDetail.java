package com.promesa.promesa.domain.exhibition.dto.response;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.exhibition.domain.Exhibition;
import lombok.Getter;

import java.util.List;

@Getter
public class ExhibitionDetail {
    private final List<ExhibitionImageResponse> images;

    private ExhibitionDetail(List<ExhibitionImageResponse> images) {
        this.images = images;
    }

    public static ExhibitionDetail of(List<ExhibitionImageResponse> images) {
        return new ExhibitionDetail(images);
    }
}
