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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/me")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "회원 프로필 정보 조회")
    public ResponseEntity<MemberResponse> getUser(@AuthenticationPrincipal CustomUserDetails user) {
        MemberResponse profile = MemberResponse.from(user.getMember());
        return ResponseEntity.ok(profile);
    }

    @PatchMapping
    @Operation(summary = "회원 프로필 정보 수정")
    public ResponseEntity<MemberResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Valid MemberUpdateRequest request
    ) {
        Member member = user.getMember();
        return ResponseEntity.ok(memberService.updateProfile(member, request));
    }

    @PatchMapping("/withdraw")
    @Operation(summary = "회원 탈퇴 처리 (논리 삭제)")
    public ResponseEntity<Void> withdraw(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        memberService.withdraw(user.getMember());
        return ResponseEntity.ok().build();
    }
}

