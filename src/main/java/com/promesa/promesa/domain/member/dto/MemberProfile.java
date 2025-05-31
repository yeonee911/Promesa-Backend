package com.promesa.promesa.domain.member.dto;

import com.promesa.promesa.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Builder
@Getter
public class MemberProfile {
    private String name; // 사용자 이름
    private String provider; // 로그인한 서비스
    // private String profileImage;
    private String providerId;

    // DTO 파일을 통하여 Entity를 생성하는 메소드
    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .provider(this.provider)
                .providerId(this.providerId)
                .build();
    }

    public static MemberProfile from(OAuth2User user) {
        return MemberProfile.builder()
                .name((String) user.getAttribute("name"))
                .provider((String) user.getAttribute("provider"))
                .providerId((String) user.getAttribute("providerId"))
                .build();
    }

}
