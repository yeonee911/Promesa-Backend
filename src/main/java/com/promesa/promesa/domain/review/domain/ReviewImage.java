package com.promesa.promesa.domain.review.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
import com.promesa.promesa.domain.member.domain.QMember;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewImage extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "review_image_id")
    private Long id;

    @Column(name = "image_key")
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public void setReview(Review review) {
        this.review = review;
    };

    @Builder
    public ReviewImage(String fileName, Long memberId, Long reviewId) {
        String key = "member/" + memberId + "/review/" + reviewId + "/" + fileName;
        this.key = key;
    }
}