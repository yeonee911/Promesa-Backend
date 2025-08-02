package com.promesa.promesa.domain.inquiry.api;

import com.promesa.promesa.domain.inquiry.application.InquiryService;
import com.promesa.promesa.domain.inquiry.dto.InquiryRequest;
import com.promesa.promesa.domain.inquiry.dto.InquiryResponse;
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
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping
    public ResponseEntity<List<InquiryResponse>> getInquiriesByArtist(@RequestParam Long artistId) {
        return ResponseEntity.ok(inquiryService.getInquiriesByArtist(artistId));
    }

    @Operation(summary = "문의 등록")
    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    public ResponseEntity<String> createInquiry (
            @RequestBody @Valid InquiryRequest request
    ) {
        return ResponseEntity.ok(inquiryService.createInquiry(request));
    }
}