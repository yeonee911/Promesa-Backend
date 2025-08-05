package com.promesa.promesa.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewImageResponse {
    private String key;
    private String url;
}