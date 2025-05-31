package com.promesa.promesa.domain.category.dao;

import com.promesa.promesa.domain.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
