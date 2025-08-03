package com.promesa.promesa.domain.item.api;

import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import com.promesa.promesa.domain.item.application.ItemInfoService;
import com.promesa.promesa.domain.item.application.ItemService;
import com.promesa.promesa.domain.item.dto.request.AddItemRequest;
import com.promesa.promesa.domain.item.dto.request.UpdateItemRequest;
import com.promesa.promesa.domain.item.dto.response.ItemResponse;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/items")
@RestController
@RequiredArgsConstructor
public class AdminItemController {

    private final ItemService itemService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "작품 등록")
    public ResponseEntity<String> createItem(
            @RequestBody @Valid AddItemRequest request
    )
    {
        String response = itemService.createItem(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "작품 수정")
    public ResponseEntity<String> updateItem(
            @RequestParam Long itemId,
            @RequestBody @Valid UpdateItemRequest request
    )
    {
        return ResponseEntity.ok(itemService.updateItem(itemId, request));
    }
}
