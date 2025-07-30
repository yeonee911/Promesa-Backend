package com.promesa.promesa.domain.member.dto.request;

public record BirthRequest(
        int year,
        int month,
        int day,
        boolean solar
) {}

