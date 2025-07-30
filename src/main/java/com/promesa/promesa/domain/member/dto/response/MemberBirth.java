package com.promesa.promesa.domain.member.dto.response;

public record MemberBirth(
        Integer birthYear,
        Integer birthMonth,
        Integer birthDay,
        Boolean isSolar
) {}
