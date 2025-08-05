package com.promesa.promesa.domain.review.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewQueryDto {
    Long reviewId;
    String content;
    Long reviewItemId;
    Long reviewOrderItemId;
    Long reviewerId;
    String reviewerName;
    int rating;
    List<String> reviewImages;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
