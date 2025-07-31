package com.promesa.promesa.domain.item.dao;

import com.promesa.promesa.domain.item.domain.Item;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByProductCode(String productCode);
    // 기본 findById 사용
}
