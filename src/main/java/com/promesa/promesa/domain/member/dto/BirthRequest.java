package com.promesa.promesa.domain.member.dto;

public record BirthRequest(
        int year,
        int month,
        int day,
        boolean solar
) {}

