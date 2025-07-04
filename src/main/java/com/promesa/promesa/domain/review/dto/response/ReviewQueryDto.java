package com.promesa.promesa.domain.review.dto.response;

import com.promesa.promesa.domain.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewQueryDto {
    private Long reviewId;
    private String content;
    private Long itemId;
    private Long reviewerId;
    private int rating;
    private List<String> reviewImages;

    public ReviewQueryDto(Long reviewId, String content, Long itemId, Long reviewerId, int rating, List<String> reviewImages) {
        this.reviewId = reviewId;
        this.content = content;
        this.itemId = itemId;
        this.reviewerId = reviewerId;
        this.rating = rating;
        this.reviewImages = reviewImages;
    }
}
