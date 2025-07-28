package com.promesa.promesa.domain.exhibition.api;

import com.promesa.promesa.domain.exhibition.application.ExhibitionService;
import com.promesa.promesa.domain.exhibition.domain.ExhibitionStatus;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionSummaryResponse;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionSummary;
import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibitions")
public class ExhibitionController {
    private final ExhibitionService exhibitionService;

    @GetMapping("/{exhibitionId}/items")
    @Operation(summary = "특정 전시 조회")
    public ResponseEntity<List<ItemPreviewResponse>> getExhibitionItems(
            @PathVariable Long exhibitionId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = (user != null) ? user.getMember() : null; // 로그인하지 않았을 경우, member = null로 전달
        List<ItemPreviewResponse> response = exhibitionService.getExhibitionItems(member, exhibitionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ongoing")
    @Operation(summary = "진행 중인 전시 목록 조회")
    public ResponseEntity<List<ExhibitionSummary>> getOngoingExhibition() {
        List<ExhibitionSummary> response = exhibitionService.getOngoingExhibitions();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "전시 상태 별 전시 목록 조회")
    public ResponseEntity<List<ExhibitionSummaryResponse>> getExhibition(
            @RequestParam(required = false) ExhibitionStatus status,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = (user != null) ? user.getMember() : null;
        List<ExhibitionSummaryResponse> responses = exhibitionService.getExhibitions(status, member);
        return  ResponseEntity.ok(responses);
    }
}
