package com.promesa.promesa.domain.artist.api;

import com.promesa.promesa.domain.artist.application.ArtistService;
import com.promesa.promesa.domain.artist.dto.ArtistNameResponse;
import com.promesa.promesa.domain.artist.dto.ArtistResponse;
import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import com.promesa.promesa.domain.item.application.ItemService;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import com.promesa.promesa.domain.exhibition.application.ExhibitionService;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionSummary;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;
    private final ExhibitionService exhibitionService;
    private final ItemService itemService;

    @GetMapping("/{artistId}")
    @Operation(summary = "작가 프로필 정보 조회")
    public ResponseEntity<ArtistResponse> getArtistProfile(
            @PathVariable Long artistId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = (user != null) ? user.getMember() : null; // 로그인하지 않았을 경우, member = null로 전달
        return ResponseEntity.ok(artistService.getArtistProfile(artistId, member));
    }

    @GetMapping("/{artistId}/exhibitions")
    @Operation(summary = "작가가 참여한 전시 목록 조회")
    public ResponseEntity<List<ExhibitionSummary>> getExhibitionsByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(exhibitionService.getExhibitionsByArtist(artistId));
    }

    @GetMapping("/{artistId}/categories/{categoryId}/items")
    @Operation(summary = "작가의 카테고리별 작품 목록 조회")
    public ResponseEntity<List<ItemPreviewResponse>> getItemsByCategory(
            @PathVariable Long artistId,
            @PathVariable Long categoryId,
            @ParameterObject Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails user
    )
    {
        Member member = (user != null) ? user.getMember() : null;
        List<ItemPreviewResponse> responses = itemService.findItemsByArtistAndCategory(member, artistId, categoryId, pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    @Operation(summary = "전체 작가 목록 조회")
    public ResponseEntity<List<ArtistResponse>> getAllArtists(@AuthenticationPrincipal CustomUserDetails user) {
        Member member = (user != null) ? user.getMember() : null;
        return ResponseEntity.ok(artistService.getAllArtists(member));
    }

    @GetMapping("/names")
    @Operation(summary = "전체 작가 이름 리스트 조회 (인덱스 탐색용)")
    public ResponseEntity<List<ArtistNameResponse>> getArtistNames() {
        return ResponseEntity.ok(artistService.getArtistNames());
    }

}
