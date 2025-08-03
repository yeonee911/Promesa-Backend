package com.promesa.promesa.domain.item.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.exhibition.domain.ExhibitionItem;
import com.promesa.promesa.domain.item.exception.InsufficientStockException;
import com.promesa.promesa.domain.itemCategory.domain.ItemCategory;
import com.promesa.promesa.domain.review.domain.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="item")
public class Item extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @NotBlank
    private String name;

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

    @Column(name = "total_rating")
    private double totalRating = 0.0;

    @NotBlank
    @Column(name = "product_code")
    private String productCode;

    private int width;
    private int height;
    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ExhibitionItem> exhibitionItems = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    private List<ItemImage> itemImages = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemCategory> itemCategories = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Item(String name, int price, int stock, SaleStatus saleStatus,
                int wishCount, Double averageRating, int reviewCount, double totalRating,
                String productCode, int width, int height, int depth, Artist artist) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.saleStatus = saleStatus;
        this.wishCount = wishCount;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.totalRating = totalRating;
        this.productCode = productCode;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.artist = artist;
    }

    public void increaseReviewCount() {
        this.reviewCount++;
    }

    public void decreaseReviewCount() {
        this.reviewCount--;
    }

    public void increaseTotalRating(int newRating) {
        this.totalRating += newRating;
    }

    public void decreaseTotalRating(int rating) {
        this.totalRating -= rating;
    }

    public void addReview(int newRating) {
        increaseReviewCount();
        increaseTotalRating(newRating);
        updateAverageRating();
    }

    public void removeReview(int rating) {
        decreaseReviewCount();
        decreaseTotalRating(rating);
        updateAverageRating();
    }

    public void updateAverageRating() {
        if (this.reviewCount == 0) {
            this.averageRating = 0.0;
        }
        else {
            this.averageRating = this.totalRating / this.reviewCount;
        }
    }

    public void increaseWishCount() {
        this.wishCount++;
    }

    public void decreaseWishCount() {
        if (this.wishCount > 0) {
            this.wishCount -= 1; 
        } else {
            this.wishCount = 0; // wishCount가 음수가 되지 않도록 방지
        }
    }

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw InsufficientStockException.EXCEPTION; // 재고 부족 예외
        }
        this.stock -= quantity;

        if (this.stock == 0) {
            this.saleStatus = SaleStatus.SOLD_OUT;
        }
    }

    public void addItemImage(ItemImage itemImage) {
        itemImage.setItem(this);
        if (!this.itemImages.contains(itemImage)) {
            this.itemImages.add(itemImage);
        }
    }

    public void addCategory(ItemCategory category) {
        category.setItem(this);
        if (!this.itemCategories.contains(category)) {
            this.itemCategories.add(category);
        }
    }

}