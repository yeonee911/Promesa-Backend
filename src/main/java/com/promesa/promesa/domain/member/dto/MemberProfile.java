package com.promesa.promesa.domain.member.dto;

import com.promesa.promesa.domain.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberProfile {
    private String name; // 사용자 이름
    private String provider; // 로그인한 서비스
    private String profileImage;

    public void setUserName(String userName) {
        this.name = userName;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    // DTO 파일을 통하여 Entity를 생성하는 메소드
    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .provider(this.provider)
                .build();
    }
}
