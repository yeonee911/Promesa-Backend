package com.promesa.promesa.domain.wish.api;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.wish.application.WishService;
import com.promesa.promesa.domain.wish.domain.TargetType;
import com.promesa.promesa.domain.wish.dto.WishResponse;
import com.promesa.promesa.domain.wish.dto.WishToggleResponse;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishes")
public class WishController {

    private final WishService wishService;

    @PostMapping
    public ResponseEntity<WishToggleResponse> addWish(
            @RequestParam TargetType targetType,
            @RequestParam Long targetId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = user.getMember();
        WishToggleResponse response = wishService.addWish(member, targetType, targetId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<WishToggleResponse> deleteWish(
            @RequestParam TargetType targetType,
            @RequestParam Long targetId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = user.getMember();
        WishToggleResponse response = wishService.deleteWish(member, targetType, targetId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<WishResponse>> getWishList(
            @RequestParam TargetType targetType,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = (user != null) ? user.getMember() : null;
        List<WishResponse> wishes = wishService.getWishList(member, targetType);
        return ResponseEntity.ok(wishes);
    }
}

