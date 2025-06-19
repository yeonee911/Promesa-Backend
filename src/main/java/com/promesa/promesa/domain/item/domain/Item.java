package com.promesa.promesa.domain.item.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.image.domain.ItemImage;
import com.promesa.promesa.domain.itemCategory.domain.ItemCategory;
import com.promesa.promesa.domain.review.domain.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Item extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private int price;
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status")
    private SaleStatus saleStatus;

    @NotNull
    @Column(name = "wish_count")
    private int wishCount = 0;

    @Column(name = "average_rating")
    private Double averageRating;   // 리뷰가 없을 경우 null 가능

    @Column(name = "review_count")
    private int reviewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ExhibitionItem> exhibitionItems = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemImage> itemImages = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemCategory> itemCategories = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public void increaseReviewCount() {
        this.reviewCount++;
    }

    public void decreaseReviewCount() {
        this.reviewCount--;
    }

    public void calculateAverageRating(int newRating) {
        if (this.averageRating == null) {   // 첫 리뷰 등록일 경우 0으로 초기화
            this.averageRating = 0.0;
        }

        double totalRating = this.averageRating * this.reviewCount; // 기존 총 평점 합
        totalRating += newRating;   // 신규 평점 추가
        increaseReviewCount();  // 리뷰 개수 증가
        this.averageRating = totalRating / this.reviewCount;    // 새로운 평균 평점 계산
    }
}