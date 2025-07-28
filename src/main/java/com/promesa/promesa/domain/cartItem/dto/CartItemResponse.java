package com.promesa.promesa.domain.cartItem.dto;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.cartItem.domain.CartItem;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.domain.ItemImage;

public record CartItemResponse(
        Long cartItemId,
        Long itemId,
        int quantity,
        String name,
        String artistName,
        int price,
        String thumbnailUrl
) {
    public static CartItemResponse of(CartItem cartItem, S3Service s3Service, String bucketName) {
        Item item = cartItem.getItem();

        String thumbnailUrl = item.getItemImages().stream()
                .filter(ItemImage::isThumbnail)
                .map(img -> s3Service.createPresignedGetUrl(bucketName, img.getImageKey()))
                .findFirst()
                .orElse(null);

        return new CartItemResponse(
                cartItem.getId(),
                item.getId(),
                cartItem.getQuantity(),
                item.getName(),
                item.getArtist().getName(),
                item.getPrice(),
                thumbnailUrl
        );
    }
}

