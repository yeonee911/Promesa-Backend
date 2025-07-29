package com.promesa.promesa.domain.member.dto;

import com.promesa.promesa.domain.member.domain.Gender;
import com.promesa.promesa.domain.shippingAddress.dto.request.AddressRequest;

public record MemberUpdateRequest(
        String phone,
        Boolean smsAgree,
        Gender gender,
        BirthRequest birth,
        AddressRequest address
) {}

