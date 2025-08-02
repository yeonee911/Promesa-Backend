package com.promesa.promesa.domain.order.domain;

import com.promesa.promesa.domain.item.domain.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;
    private int price;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus orderItemStatus;

    public int getTotalPrice() {
        return quantity * price;
    }

    public static OrderItem of(Order order, Item item, int quantity, int price) {
        return OrderItem.builder()
                .order(order)
                .item(item)
                .quantity(quantity)
                .price(price)
                .build();
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

