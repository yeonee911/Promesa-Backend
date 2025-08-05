package com.promesa.promesa.domain.review.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImageResponse {
    private String key;
    private String url;

    public ReviewImageResponse(String key) {
        this.key = key;
    }
}