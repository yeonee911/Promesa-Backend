package com.promesa.promesa.domain.wish.dto;

import com.promesa.promesa.domain.wish.domain.TargetType;
import com.promesa.promesa.domain.wish.domain.Wish;

public record WishResponse(
        TargetType targetType,  // ITEM or ARTIST
        Long targetId,          // 작품 ID 또는 작가 ID
        String title,           // 작품명 또는 작가명
        String thumbnailUrl     // 썸네일 이미지 (nullable 가능)
) {
    public static WishResponse from(Wish wish, String title, String thumbnailUrl) {
        return new WishResponse(
                wish.getTargetType(),
                wish.getTargetId(),
                title,
                thumbnailUrl
        );
    }
}

