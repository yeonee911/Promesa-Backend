package com.promesa.promesa.domain.home.api;

import com.promesa.promesa.domain.home.application.HomeService;
import com.promesa.promesa.domain.home.dto.response.BrandInfoResponse;
import com.promesa.promesa.domain.home.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HomeController {
    private final HomeService homeService;

    @GetMapping("/brand-info")
    public ResponseEntity<BrandInfoResponse> getBrandInfo() {
        return ResponseEntity.ok(homeService.getBrandInfo());
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> getBrandInfo(@RequestParam String keyword) {
        return homeService.search(keyword);
    }
}
