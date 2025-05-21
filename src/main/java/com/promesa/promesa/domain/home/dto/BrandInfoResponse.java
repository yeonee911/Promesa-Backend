package com.promesa.promesa.domain.home.dto;

public record BrandInfoResponse(
        String mainImageUrl,
        String brandStory
) {
    public static final BrandInfoResponse INFO = new BrandInfoResponse(
            "static/brand-image.jpg",
            "도예과 학생들의 작품을 세상에 소개하는 플랫폼입니다."
    );
}