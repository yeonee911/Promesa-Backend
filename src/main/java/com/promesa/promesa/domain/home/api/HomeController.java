package com.promesa.promesa.domain.home.api;

import com.promesa.promesa.domain.home.application.HomeService;
import com.promesa.promesa.domain.home.dto.response.BrandInfoResponse;
import com.promesa.promesa.domain.home.dto.response.SearchResponse;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HomeController {
    private final HomeService homeService;

    @GetMapping("/brand-info")
    @Operation(summary = "브랜드 정보")
    public ResponseEntity<BrandInfoResponse> getBrandInfo() {
        return ResponseEntity.ok(homeService.getBrandInfo());
    }

    @GetMapping("/search")
    @Operation(summary = "검색")
    public ResponseEntity<SearchResponse> getBrandInfo(
            @RequestParam String keyword,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = (user != null) ? user.getMember() : null;
        return ResponseEntity.ok(homeService.search(keyword, member));
    }
}
