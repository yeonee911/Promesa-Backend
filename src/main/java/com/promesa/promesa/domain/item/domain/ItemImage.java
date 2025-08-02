package com.promesa.promesa.domain.item.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_image_id")
    private Long id;

    @NotBlank
    @Column(name = "item_image_key")
    private String imageKey;

    @NotNull
    private boolean isThumbnail;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public ItemImage(String imageKey, boolean isThumbnail, Integer sortOrder, Item item) {
        this.imageKey = imageKey;
        this.isThumbnail = isThumbnail;
        this.sortOrder = sortOrder;
        this.item = item;
    }

    public void setItem(Item item) {
        this.item = item;
        if (!item.getItemImages().contains(this)) {
            item.getItemImages().add(this);
        }
    }
}
