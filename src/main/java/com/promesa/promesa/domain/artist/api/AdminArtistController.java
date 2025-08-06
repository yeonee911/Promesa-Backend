package com.promesa.promesa.domain.artist.api;

import com.promesa.promesa.domain.artist.application.ArtistService;
import com.promesa.promesa.domain.artist.dto.request.AddArtistRequest;
import com.promesa.promesa.domain.artist.dto.request.UpdateArtistImageRequest;
import com.promesa.promesa.domain.artist.dto.request.UpdateArtistInfoRequest;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/artists")
public class AdminArtistController {
    private final ArtistService artistService;

    @PostMapping
    @Operation(summary = "작가 등록")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createArtist(
            @RequestBody @Valid AddArtistRequest request)
    {
        return ResponseEntity.ok(artistService.createArtist(request));
    }

    @PatchMapping("/{artistId}")
    @Operation(summary = "작가 정보 수정")
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    public ResponseEntity<String> updateArtistInfo(
            @PathVariable Long artistId,
            @RequestBody @Valid UpdateArtistInfoRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    )
    {
        Member member = (user != null) ? user.getMember() : null;
        return ResponseEntity.ok(artistService.updateArtistInfo(artistId, request, member));
    }

    @PatchMapping("/{artistId}/profile-image")
    @Operation(summary = "작가 프로필 이미지 변경")
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    public ResponseEntity<String> updateArtistProfile(
            @PathVariable Long artistId,
            @RequestBody @Valid UpdateArtistImageRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    )
    {
        Member member = (user != null) ? user.getMember() : null;
        return ResponseEntity.ok(artistService.updateArtistImage(artistId, request, member));
    }
}
