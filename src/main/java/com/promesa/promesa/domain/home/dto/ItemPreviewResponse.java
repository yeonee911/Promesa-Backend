package com.promesa.promesa.domain.home.dto;

public record ItemPreviewResponse(
        Long itemId,
        String itemName,
        String itemDescription,
        int price,
        String thumnailUrl,
        String artistName,
        boolean isWished
) {}