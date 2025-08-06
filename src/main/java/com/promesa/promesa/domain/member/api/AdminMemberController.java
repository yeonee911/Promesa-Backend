package com.promesa.promesa.domain.member.api;

import com.promesa.promesa.domain.member.application.MemberService;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.dto.request.MemberUpdateRequest;
import com.promesa.promesa.domain.member.dto.response.MemberResponse;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    @Operation(summary = "USER 권한의 회원 조회")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MemberResponse>> getAllUser() {
        return ResponseEntity.ok(memberService.getAllUser());
    }
}

