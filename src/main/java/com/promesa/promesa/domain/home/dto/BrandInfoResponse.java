package com.promesa.promesa.domain.home.dto;

public record BrandInfoResponse(
        String mainImageUrl,
        String leftImageUrl,
        String rightImageUrl,
        String brandStory
) {
    public static final BrandInfoResponse INFO = new BrandInfoResponse(
            "https://ceos-promesa.s3.ap-northeast-2.amazonaws.com/brand-info/mainImage.png",
            "https://ceos-promesa.s3.ap-northeast-2.amazonaws.com/brand-info/mainImage.png",
            "https://ceos-promesa.s3.ap-northeast-2.amazonaws.com/brand-info/mainImage.png",
            "프로메사는 숨겨진 신예 작가들을 발굴하고 \n" +
                    "작품의 전시와 거래를 돕는 플랫폼입니다.\n" +
                    "\n" +
                    "도예를 전공하는 학생, 작품은 있지만 판매에 미숙한 작가, \n" +
                    "유일무이한 오브제로 일상에 가치를 더하고 싶은 소비자까지.\n" +
                    "저희 프로메사는 도예를 사랑하는 모두를 위해\n" +
                    "열정을 다하겠습니다."
    );
}