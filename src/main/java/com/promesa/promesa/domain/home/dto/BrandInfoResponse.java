package com.promesa.promesa.domain.home.dto;

public record BrandInfoResponse(
        String mainImageUrl,
        String leftImageUrl,
        String rightImageUrl,
        String brandStory
) {
    private static final String BRAND_STORY = """
        팀 프로메사는 아직 주목받지 못한 신예 작가들을 발굴하고 
        플랫폼을 통해 작품의 전시와 거래를 돕습니다.

        도예를 전공하는 학생, 작품은 있지만 판매에 미숙한 작가, 
        유일무이한 오브제로 일상에 가치를 더하고 싶은 소비자까지.
        저희 프로메사는 도예를 사랑하는 모두를 위해
        열정을 다하겠습니다.
        """;

    public static BrandInfoResponse of(String mainUrl, String leftUrl, String rightUrl) {
        return new BrandInfoResponse(mainUrl, leftUrl, rightUrl, BRAND_STORY);
    }
}