package com.promesa.promesa.domain.cartItem.api;

import com.promesa.promesa.domain.cartItem.application.CartItemService;
import com.promesa.promesa.domain.cartItem.dto.CartItemAddRequest;
import com.promesa.promesa.domain.cartItem.dto.CartItemResponse;
import com.promesa.promesa.domain.cartItem.dto.CartItemUpdateRequest;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    @Operation(summary = "장바구니 아이템 추가")
    public ResponseEntity<CartItemResponse> addToCart(@AuthenticationPrincipal CustomUserDetails user,
                                          @RequestBody CartItemAddRequest request) {
        Member member = user.getMember();
        return ResponseEntity.ok(cartItemService.addToCart(member, request));
    }

    @PatchMapping
    @Operation(summary = "장바구니 아이템 수량 수정")
    public ResponseEntity<CartItemResponse> updateCartItem(@AuthenticationPrincipal CustomUserDetails user,
                                               @RequestBody CartItemUpdateRequest request) {
        Member member = user.getMember();
        return ResponseEntity.ok(cartItemService.updateCartItem(member, request));
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "장바구니 아이템 삭제")
    public ResponseEntity<CartItemResponse> deleteCartItem(@AuthenticationPrincipal CustomUserDetails user,
                                               @PathVariable Long itemId) {
        Member member = user.getMember();
        return ResponseEntity.ok(cartItemService.deleteCartItem(member, itemId));
    }

    @GetMapping
    @Operation(summary = "장바구니 아이템 목록 조회")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        return ResponseEntity.ok(cartItemService.getCartItems(member));
    }
}
