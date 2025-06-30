package com.promesa.promesa.domain.member.dto;

import com.promesa.promesa.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Builder
@Getter
public class MemberProfile {
    private String name;
    private String provider;
    private String providerId;

    // JWT 인증 사용자용
    public static MemberProfile from(Member member) {
        return MemberProfile.builder()
                .name(member.getName())
                .provider(member.getProvider())
                .providerId(member.getProviderId())
                .build();
    }

    // OAuth2 로그인 사용자용
    public static MemberProfile from(OAuth2User user) {
        return MemberProfile.builder()
                .name((String) user.getAttribute("name"))
                .provider((String) user.getAttribute("provider"))
                .providerId((String) user.getAttribute("providerId"))
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .provider(this.provider)
                .providerId(this.providerId)
                .build();
    }
}
