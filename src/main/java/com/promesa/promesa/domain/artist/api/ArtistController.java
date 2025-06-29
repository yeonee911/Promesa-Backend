package com.promesa.promesa.domain.artist.api;

import com.promesa.promesa.domain.artist.application.ArtistService;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.dto.ArtistResponse;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import com.promesa.promesa.domain.exhibition.application.ExhibitionService;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
        return ResponseEntity.ok(artistService.getArtistProfile(artistId, user.getMember()));
    }

    @GetMapping("/{artistId}/exhibitions")
    @Operation(summary = "작가가 참여한 전시 목록 조회")
    public ResponseEntity<List<ExhibitionResponse>> getExhibitionsByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(exhibitionService.getExhibitionsByArtist(artistId));
    }
}
