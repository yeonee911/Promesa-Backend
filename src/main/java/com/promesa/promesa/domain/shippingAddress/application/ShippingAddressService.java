package com.promesa.promesa.domain.shippingAddress.application;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.shippingAddress.dao.ShippingAddressRepository;
import com.promesa.promesa.domain.shippingAddress.domain.ShippingAddress;
import com.promesa.promesa.domain.shippingAddress.dto.AddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShippingAddressService {

    public AddressResponse getShippingAddress(Member member) {
        ShippingAddress address = member.getShippingAddress();
        AddressResponse response = AddressResponse.from(address);

        return response;
    }
}
