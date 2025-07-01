package com.promesa.promesa.domain.artist.api;

import com.promesa.promesa.domain.artist.application.ArtistService;
import com.promesa.promesa.domain.artist.dto.ArtistResponse;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import com.promesa.promesa.domain.exhibition.application.ExhibitionService;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;
    private final ExhibitionService exhibitionService;

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistResponse> getArtistProfile(
            @PathVariable Long artistId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = (user != null) ? user.getMember() : null; // 로그인하지 않았을 경우, member = null로 전달
        return ResponseEntity.ok(artistService.getArtistProfile(artistId, member));
    }

    @GetMapping("/{artistId}/exhibitions")
    @Operation(summary = "작가가 참여한 전시 목록 조회")
    public ResponseEntity<List<ExhibitionResponse>> getExhibitionsByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(exhibitionService.getExhibitionsByArtist(artistId));
    }
}
