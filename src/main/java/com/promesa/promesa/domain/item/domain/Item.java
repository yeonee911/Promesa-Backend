package com.promesa.promesa.domain.item.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Item {
    @Id @GeneratedValue
    private Long id;
}
