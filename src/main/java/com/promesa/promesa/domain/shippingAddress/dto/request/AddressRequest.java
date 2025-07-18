package com.promesa.promesa.domain.shippingAddress.dto.request;

import com.promesa.promesa.domain.shippingAddress.domain.ShippingAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private String recipientName;
    private String zipCode;
    private String addressMain;
    private String addressDetails;
    private String recipientPhone;
}