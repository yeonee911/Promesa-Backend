package com.promesa.promesa.domain.inquiry.api;

import com.promesa.promesa.domain.inquiry.application.InquiryService;
import com.promesa.promesa.domain.inquiry.dto.InquiryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
