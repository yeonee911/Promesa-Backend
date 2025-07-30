package com.promesa.promesa.domain.member;

import com.promesa.promesa.domain.member.dto.response.MemberProfile;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    KAKAO("kakao", (attribute) -> {
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");

        return MemberProfile.builder()
                .name((String) properties.get("nickname"))
                .provider("kakao")
                .providerId(attribute.get("id").toString())
                .build();
    });


    private final String registrationId; // 로그인한 서비스(ex) google, naver..)
    private final Function<Map<String, Object>, MemberProfile> of; // 로그인한 사용자의 정보를 통하여 MemberProfile을 가져옴

    OAuthAttributes(String registrationId, Function<Map<String, Object>, MemberProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static MemberProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
