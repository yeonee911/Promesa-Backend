package com.promesa.promesa.domain.item.api;

import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import com.promesa.promesa.domain.item.application.ItemService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/categories/{categoryId}/items")
    public ResponseEntity<Page<ItemPreviewResponse>> findCategoryItem(
            @PathVariable Long categoryId,
            @RequestParam Long memberId,
            Pageable pageable)
    {
        Page<ItemPreviewResponse> response = itemService.findCategoryItem(memberId, categoryId, pageable);
        return ResponseEntity.ok(response);
    }
}
