package com.promesa.promesa.domain.itemCategory.service;

import com.promesa.promesa.domain.category.dao.CategoryRepository;
import com.promesa.promesa.domain.category.domain.Category;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.itemCategory.domain.ItemCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemCategoryService {

    private final CategoryRepository categoryRepository;

    public void changeCategory(Item item, Category category) {
        item.getItemCategories().clear();    // 기존 카테고리 없앰
        ItemCategory newItemCategory = ItemCategory.builder()
                .category(category)
                .item(item)
                .build();
        item.addCategory(newItemCategory);
    }
}
