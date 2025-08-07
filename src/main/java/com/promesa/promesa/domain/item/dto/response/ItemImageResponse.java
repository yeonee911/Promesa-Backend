package com.promesa.promesa.domain.item.dto.response;

public record ItemImageResponse(
        String url,
        String imageKey,
        int sortOrder
) {}