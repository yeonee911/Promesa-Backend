package com.promesa.promesa.domain.member.api;

import com.promesa.promesa.domain.member.dto.MemberProfile;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class MemberController {

    @GetMapping("/me")
    public ResponseEntity<MemberProfile> getUser(@AuthenticationPrincipal CustomUserDetails user) {
        MemberProfile profile = MemberProfile.from(user.getMember());
        return ResponseEntity.ok(profile);
    }
}

