package com.promesa.promesa.domain.delivery.domain;

import com.promesa.promesa.domain.order.domain.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String courierName; // 택배사

    private String receiverName;          // 수령인 이름
    private String receiverPhone;         // 수령인 연락처
    private String zipCode;               // 우편번호
    private String address;               // 수령 주소 (도로명 또는 지번)
    private String addressDetail;         // 수령 상세 주소 (동/호수 등)

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private LocalDateTime deliveryExpectedDate; // 배송 예정일
    private LocalDateTime deliveryStartDate; // 배송 시작일
    private LocalDateTime deliveryCompletedDate; // 배송 완료일
}

