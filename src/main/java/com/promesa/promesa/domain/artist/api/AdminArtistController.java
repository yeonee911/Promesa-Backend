package com.promesa.promesa.domain.artist.api;

import com.promesa.promesa.domain.artist.application.ArtistService;
import com.promesa.promesa.domain.artist.dto.request.AddArtistRequest;
import com.promesa.promesa.domain.artist.dto.response.ArtistNameResponse;
import com.promesa.promesa.domain.artist.dto.response.ArtistResponse;
import com.promesa.promesa.domain.exhibition.application.ExhibitionService;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionSummary;
import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import com.promesa.promesa.domain.item.application.ItemService;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artists")
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
}
