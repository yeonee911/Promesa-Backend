package com.promesa.promesa.domain.wish.dto;

import com.promesa.promesa.domain.wish.domain.TargetType;
import com.promesa.promesa.domain.wish.domain.Wish;

public record WishResponse(
        TargetType targetType,  // ITEM or ARTIST
        Long targetId,          // 작품 ID 또는 작가 ID
        String title,           // 작품명 or 작가명
        String thumbnailUrl,    // 썸네일 이미지 (nullable 가능)
        String artistName,      // 작품의 작가명 (ITEM일 경우만)
        int price               // 작품 가격 (ITEM일 경우만)
) {
    // ITEM용 팩토리 메서드
    public static WishResponse fromItem(Wish wish, String title, String thumbnailUrl, String artistName, int price) {
        return new WishResponse(
                wish.getTargetType(),
                wish.getTargetId(),
                title,
                thumbnailUrl,
                artistName,
                price
        );
    }

    // ARTIST용 팩토리 메서드
    public static WishResponse fromArtist(Wish wish, String title, String thumbnailUrl) {
        return new WishResponse(
                wish.getTargetType(),
                wish.getTargetId(),
                title,
                thumbnailUrl,
                null,  // 작가명이므로 null
                0      // 가격 없음
        );
    }
}
