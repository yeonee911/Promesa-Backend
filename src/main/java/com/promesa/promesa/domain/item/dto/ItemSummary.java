package com.promesa.promesa.domain.item.dto;

import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

public record ItemSummary(
        Long itemId,
        Long categoryId,
        String categoryName,
        String title,
        List<String> imageUrls,
        double averageRating,
        int reviewCount,
        Long artistId
) {}
