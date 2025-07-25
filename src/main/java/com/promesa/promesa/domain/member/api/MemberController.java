package com.promesa.promesa.domain.member.api;

import com.promesa.promesa.domain.member.application.MemberService;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.dto.MemberProfile;
import com.promesa.promesa.domain.member.dto.MemberUpdateRequest;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth/me")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "회원 프로필 정보 조회")
    public ResponseEntity<MemberProfile> getUser(@AuthenticationPrincipal CustomUserDetails user) {
        MemberProfile profile = MemberProfile.from(user.getMember());
        return ResponseEntity.ok(profile);
    }

    @PatchMapping
    @Operation(summary = "회원 프로필 정보 수정")
    public ResponseEntity<MemberProfile> updateProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Valid MemberUpdateRequest request
    ) {
        Member member = user.getMember();
        return ResponseEntity.ok(memberService.updateProfile(member, request));
    }
}

