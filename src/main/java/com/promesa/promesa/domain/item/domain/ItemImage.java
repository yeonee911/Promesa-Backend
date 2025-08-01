package com.promesa.promesa.domain.item.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage extends BaseTimeEntity {
    @Id @GeneratedValue
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
}
