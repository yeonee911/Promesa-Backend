package com.promesa.promesa.domain.wish.api;

import com.promesa.promesa.domain.artist.dto.response.ArtistWish;
import com.promesa.promesa.domain.item.dto.response.ItemWish;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.wish.application.WishService;
import com.promesa.promesa.domain.wish.domain.TargetType;
import com.promesa.promesa.domain.wish.dto.WishResponse;
import com.promesa.promesa.domain.wish.dto.WishToggleResponse;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Operation(summary = "위시리스트/북마크 추가")
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
    @Operation(summary = "위시리스트/북마크 삭제")
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
    @Operation(summary = "내 위시리스트/북마크 목록 조회")
    public ResponseEntity<List<WishResponse>> getWishList(
            @RequestParam TargetType targetType,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = (user != null) ? user.getMember() : null;
        List<WishResponse> wishes = wishService.getWishList(member, targetType);
        return ResponseEntity.ok(wishes);
    }

/*
    @GetMapping("/item/{itemId}")
    @Operation(summary = "작품 위시리스트 여부 조회")
    public ResponseEntity<ItemWish> getItemWish(
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomUserDetails user) {
        Member member = (user != null) ? user.getMember() : null;
        return ResponseEntity.ok(wishService.getItemWish(itemId, member));
    }

    @GetMapping("/artist/{artistId}")
    @Operation(summary = "작가 북마크 여부 조회")
    public ResponseEntity<ArtistWish> getArtistWish(
            @PathVariable Long artistId,
            @AuthenticationPrincipal CustomUserDetails user) {
        Member member = (user != null) ? user.getMember() : null;
        return ResponseEntity.ok(wishService.getArtistWish(artistId, member));

    }
 */
}

