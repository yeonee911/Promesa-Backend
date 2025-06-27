package com.promesa.promesa.security.oauth;

import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // ex. kakao
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String providerId = extractProviderId(provider, attributes);
        String name = extractName(provider, attributes);

        // DB 조회 또는 저장
        Member member = memberRepository
                .findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .name(name)
                        .provider(provider)
                        .providerId(providerId)
                        .build()));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "name" // default key
        );
    }

    private String extractProviderId(String provider, Map<String, Object> attributes) {
        if (provider.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            return String.valueOf(attributes.get("id"));
        }
        // TODO: Add Google, Naver, etc
        throw new RuntimeException("지원하지 않는 provider: " + provider);
    }

    private String extractName(String provider, Map<String, Object> attributes) {
        if (provider.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            return (String) profile.get("nickname");
        }
        return "unknown";
    }
}
