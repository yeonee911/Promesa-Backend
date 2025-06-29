package com.promesa.promesa.domain.exhibition.api;

import com.promesa.promesa.domain.exhibition.application.ExhibitionService;
import com.promesa.promesa.domain.exhibition.dto.response.ExhibitionResponse;
import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibitions")
public class ExhibitionController {
    private final ExhibitionService exhibitionService;

    @GetMapping("/{exhibitionId}/items")
    public ResponseEntity<List<ItemPreviewResponse>> getExhibitionItems(
            @PathVariable Long exhibitionId,
            @RequestParam(required = false) Long memberId // 요청 URL에 ?memberId=1 붙여야 함. 개발 테스트용
            // 로그인 기능 구현 시 @AuthenticationPrincipal
    ) {
        List<ItemPreviewResponse> response = exhibitionService.getExhibitionItems(memberId, exhibitionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "진행 중인 전시 목록 조회")
    public ResponseEntity<List<ExhibitionResponse>> getOngoingExhibition() {
        List<ExhibitionResponse> response = exhibitionService.getOngoingExhibitions();
        return ResponseEntity.ok(response);
    }
}
