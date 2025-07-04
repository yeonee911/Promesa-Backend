package com.promesa.promesa.domain.wish.domain;

public enum TargetType {
    ARTIST, ITEM;

    public static TargetType from(String value) {
        return TargetType.valueOf(value.toUpperCase());
    }
}
