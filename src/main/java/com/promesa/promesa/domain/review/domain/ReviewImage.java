package com.promesa.promesa.domain.review.domain;

import jakarta.persistence.*;

@Entity
public class ReviewImage {
    @Id @GeneratedValue
    @Column(name = "review_image_id")
    private Long id;

    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public void setReview(Review review) {
        this.review = review;
    };
}