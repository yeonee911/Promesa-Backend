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
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping
    public ResponseEntity<List<InquiryResponse>> getInquiriesByArtist(@RequestParam Long artistId) {
        return ResponseEntity.ok(inquiryService.getInquiriesByArtist(artistId));
    }
}