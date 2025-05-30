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
    private String profileImage;

    // DTO 파일을 통하여 Entity를 생성하는 메소드
    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .provider(this.provider)
                .build();
    }

    public static MemberProfile from(OAuth2User user) {
        return MemberProfile.builder()
                .name((String) user.getAttribute("name")) // 이 key들은 OAuth2Service에서 넣어줘야 함
                .provider((String) user.getAttribute("provider"))
                .profileImage((String) user.getAttribute("profileImage"))
                .build();
    }

}
