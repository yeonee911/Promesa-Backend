package com.promesa.promesa.domain.item.api;

import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import com.promesa.promesa.domain.item.application.ItemService;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/categories/{categoryId}/items")
    public ResponseEntity<Page<ItemPreviewResponse>> findCategoryItem(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetails user,
            @ParameterObject Pageable pageable)
    {
        Member member = (user != null) ? user.getMember() : null;
        Page<ItemPreviewResponse> response = itemService.findCategoryItem(member, categoryId, pageable);
        return ResponseEntity.ok(response);
    }
}
