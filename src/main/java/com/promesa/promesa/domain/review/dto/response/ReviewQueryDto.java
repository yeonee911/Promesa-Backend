package com.promesa.promesa.domain.review.dto.response;

import com.promesa.promesa.domain.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewQueryDto {
    private Long reviewId;
    private String content;
    private Long itemId;
    private Long reviewerId;
    private String reviewerName;
    private int rating;
    private List<String> reviewImages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
