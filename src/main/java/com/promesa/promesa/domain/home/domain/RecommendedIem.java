package com.promesa.promesa.domain.home.domain;

import com.promesa.promesa.domain.item.domain.Item;
import jakarta.persistence.*;

@Entity
public class RecommendedIem {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private int priority;   // 노출 순서
}
