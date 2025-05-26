package com.promesa.promesa.domain.home.dto;

public record BrandInfoResponse(
        String mainImageUrl,
        String brandStory
) {
    public static final BrandInfoResponse INFO = new BrandInfoResponse(
            "https://ceos-promesa.s3.ap-northeast-2.amazonaws.com/brand-info/home-image.png",
            "절제된 선과 형태 속에 담긴 작가의 시선.\n" +
                    "공간에 울림을 더하는 모던 도자 컬렉션을 만나보세요."
    );
}