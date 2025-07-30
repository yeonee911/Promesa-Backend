package com.promesa.promesa.domain.member.dto.response;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.shippingAddress.domain.ShippingAddress;

public record MemberResponse(
        MemberBasic profile,
        MemberBirth birth,
        MemberAddress address
) {
    public static MemberResponse from(Member member) {
        ShippingAddress addr = member.getShippingAddress();

        return new MemberResponse(
                new MemberBasic(
                        member.getName(),
                        member.getProvider(),
                        member.getProviderId(),
                        member.getPhone(),
                        member.getSmsAgree(),
                        member.getGender()
                ),
                new MemberBirth(
                        member.getBirthYear(),
                        member.getBirthMonth(),
                        member.getBirthDay(),
                        member.getIsSolar()
                ),
                addr == null ? null :
                        new MemberAddress(
                                addr.getRecipientName(),
                                addr.getZipCode(),
                                addr.getAddressMain(),
                                addr.getAddressDetails(),
                                addr.getRecipientPhone()
                        )
        );
    }
}
