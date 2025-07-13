package com.promesa.promesa.domain.shippingAddress.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ShippingAddress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_address_id")
    private Long id;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address_main")
    private String addressMain;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "recipient_phone")
    private String recipientPhone;
}
