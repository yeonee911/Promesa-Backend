package com.promesa.promesa.domain.item.dto.response;

import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.category.domain.Category;
import com.promesa.promesa.domain.item.domain.Item;

import java.util.List;
import java.util.Optional;

public record ItemSummary(
        Long itemId,
        Long categoryId,
        String categoryName,
        String title,
        List<ItemImageResponse> mainImageUrls,
        List<ItemImageResponse> detailImageUrls,
        double averageRating,
        int reviewCount,
        Long artistId
) {
    public static ItemSummary from(
            Item item,
            Category category,
            List<ItemImageResponse> mainImages,
            List<ItemImageResponse> detailImages,
            Artist artist
    ) {
        return new ItemSummary(
                item.getId(),
                category.getId(),
                category.getName(),
                item.getName(),
                mainImages,
                detailImages,
                Optional.ofNullable(item.getAverageRating()).orElse(0.0),
                item.getReviewCount(),
                artist.getId()
        );
    }
}
