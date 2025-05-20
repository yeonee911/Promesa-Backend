package com.promesa.promesa.domain.home.dto;

import java.util.List;

public record HomeReponse (
        BrandInfoResponse brandInfo,
        List<ItemPreviewResponse> recommendedItems
){}
