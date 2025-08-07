package com.promesa.promesa.domain.exhibition.dto.response;

public record ExhibitionImageResponse(
        String detailedImageUrl,
        String detailImageKey,
        int sortOrder
) {}