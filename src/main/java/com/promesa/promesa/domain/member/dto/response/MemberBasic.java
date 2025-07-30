package com.promesa.promesa.domain.member.dto.response;

import com.promesa.promesa.domain.member.domain.Gender;

public record MemberBasic(
        String name,
        String provider,
        String providerId,
        String phone,
        Boolean smsAgree,
        Gender gender
) {}
