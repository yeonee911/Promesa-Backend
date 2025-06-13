package com.promesa.promesa.domain.category.application;

import com.promesa.promesa.domain.category.dao.CategoryRepository;
import com.promesa.promesa.domain.category.domain.Category;
import com.promesa.promesa.domain.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getParentCategories() {
        return categoryRepository.findByParentIsNull().stream()
                .map(CategoryResponse::from)
                .toList();
    }


    public List<CategoryResponse> getChildCategories(Long parentId){
        return categoryRepository.findByParentId(parentId).stream()
                .map(CategoryResponse::from)
                .toList();
    }
}
