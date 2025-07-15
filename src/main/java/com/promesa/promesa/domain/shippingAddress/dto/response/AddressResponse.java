package com.promesa.promesa.domain.shippingAddress.dto.response;

import com.promesa.promesa.domain.shippingAddress.domain.ShippingAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponse {
    private final String recipientName;
    private final String zipCode;
    private final String addressMain;
    private final String addressDetails;
    private final String recipientPhone;

    public static AddressResponse from(ShippingAddress address) {
        return AddressResponse.builder()
                .recipientName(address.getRecipientName())
                .zipCode(address.getZipCode())
                .addressMain(address.getAddressMain())
                .addressDetails(address.getAddressDetails())
                .recipientPhone(address.getRecipientPhone())
                .build();
    }
}
