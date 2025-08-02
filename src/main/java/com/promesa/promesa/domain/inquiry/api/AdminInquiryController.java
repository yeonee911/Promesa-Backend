package com.promesa.promesa.domain.inquiry.api;

import com.promesa.promesa.domain.inquiry.application.InquiryService;
import com.promesa.promesa.domain.inquiry.dto.request.AddInquiryRequest;
import com.promesa.promesa.domain.inquiry.dto.request.UpdateInquiryRequest;
import com.promesa.promesa.domain.inquiry.dto.response.InquiryResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inquiries")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "문의 등록")
    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    public ResponseEntity<String> createInquiry (
            @RequestBody @Valid AddInquiryRequest request
    ) {
        return ResponseEntity.ok(inquiryService.createInquiry(request));
    }

    @Operation(summary = "문의 삭제")
    @DeleteMapping("/{inquiryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    public ResponseEntity<String> deleteInquiry (@PathVariable Long inquiryId) {
        return ResponseEntity.ok(inquiryService.deleteInquiry(inquiryId));
    }

    @Operation(summary = "문의 수정")
    @PutMapping("/{inquiryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    public ResponseEntity<String> updateInquiry (
            @PathVariable Long inquiryId,
            @RequestBody @Valid UpdateInquiryRequest request
    ) {
        return ResponseEntity.ok(inquiryService.updateInquiry(inquiryId, request));
    }
}