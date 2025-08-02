package com.promesa.promesa.domain.exhibition.api;

import com.promesa.promesa.domain.exhibition.application.ExhibitionService;
import com.promesa.promesa.domain.exhibition.domain.ExhibitionStatus;
import com.promesa.promesa.domain.exhibition.dto.request.AddExhibitionRequest;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionDetailResponse;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionSummary;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionSummaryResponse;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibitions")
public class AdminExhibitionController {
    private final ExhibitionService exhibitionService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "기획전 등록")
    public ResponseEntity<String> createExhibition(
            @RequestBody @Valid AddExhibitionRequest request
    )
    {
        return ResponseEntity.ok(exhibitionService.createExhibition(request));
    }
}
