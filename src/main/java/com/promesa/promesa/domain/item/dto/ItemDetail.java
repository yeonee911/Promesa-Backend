package com.promesa.promesa.domain.item.dto;

public record ItemDetail(
        String productCode,
        String type,
        int width,
        int height,
        int depth
) {}

