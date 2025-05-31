package com.promesa.promesa.domain.member.api;

import com.promesa.promesa.domain.member.dto.MemberProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class MemberController {

    @GetMapping("/loginInfo")
    public String getJson(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return attributes.toString();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberProfile> getUser(@AuthenticationPrincipal OAuth2User user) {
        MemberProfile profile = MemberProfile.from(user);
        return ResponseEntity.ok(profile);
    }
}

