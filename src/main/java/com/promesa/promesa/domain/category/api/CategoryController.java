package com.promesa.promesa.domain.category.api;

import com.promesa.promesa.domain.category.application.CategoryService;
import com.promesa.promesa.domain.category.domain.Category;
import com.promesa.promesa.domain.category.dto.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/parent")
    @Operation(summary = "작품 카테고리 조회")
    public ResponseEntity<List<CategoryResponse>> getParentCategories() {
        return ResponseEntity.ok(categoryService.getParentCategories());
    }
/*
    @GetMapping("/child")
    @Operation(summary = "작품 하위 카테고리 조회")
    public ResponseEntity<List<CategoryResponse>> getChildCategories(@RequestParam Long parentId) {
        return ResponseEntity.ok(categoryService.getChildCategories(parentId));
    }
 */
}
