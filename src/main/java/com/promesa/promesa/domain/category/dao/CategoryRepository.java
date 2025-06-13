package com.promesa.promesa.domain.category.dao;

import com.promesa.promesa.domain.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNull();

    List<Category> findByParentId(Long parentId);
}
