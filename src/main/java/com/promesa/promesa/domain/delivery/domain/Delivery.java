package com.promesa.promesa.domain.delivery.domain;

import com.promesa.promesa.domain.delivery.dto.request.DeliveryRequest;
import com.promesa.promesa.domain.order.domain.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    private String courierName; // 택배사

    private String receiverName;          // 수령인 이름
    private String receiverPhone;         // 수령인 연락처
    private String zipCode;               // 우편번호
    private String address;               // 수령 주소 (도로명 또는 지번)
    private String addressDetail;         // 수령 상세 주소 (동/호수 등)

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private LocalDate deliveryExpectedDate; // 배송 예정일
    private LocalDate deliveryStartDate; // 배송 시작일
    private LocalDate deliveryCompletedDate; // 배송 완료일

    private int deliveryFee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
    }

    public void updateFrom(DeliveryRequest request) {
        this.courierName = request.courierName();
        this.receiverName = request.receiverName();
        this.receiverPhone = request.receiverPhone();
        this.zipCode = request.zipCode();
        this.address = request.address();
        this.addressDetail = request.addressDetail();
        this.deliveryStatus = request.deliveryStatus();
        this.deliveryExpectedDate = request.deliveryExpectedDate();
        this.deliveryStartDate = request.deliveryStartDate();
        this.deliveryCompletedDate = request.deliveryCompletedDate();
        this.deliveryFee = request.deliveryFee();
    }

}

