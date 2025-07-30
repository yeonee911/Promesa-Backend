package com.promesa.promesa.domain.member.dto.response;

public record MemberAddress(
        String recipientName,
        String zipCode,
        String addressMain,
        String addressDetails,
        String recipientPhone
) {}
