package com.promesa.promesa.common.config.S3;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageUrlDto {
    private final String url;   // 클라이언트가 사용할 업로드 URL
    private final String key;   // S3에 저장될 키

    public static ImageUrlDto of(String url, String key) {
        return ImageUrlDto.builder()
                .key(key)
                .url(url)
                .build();
    }
}
