package com.promesa.promesa.common.application;

import com.promesa.promesa.domain.member.OAuthAttributes;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.domain.Role;
import com.promesa.promesa.domain.member.dto.response.MemberProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 로그인을 수행한 서비스의 이름
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); // PK가 되는 정보
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 사용자가 가지고 있는 정보
        MemberProfile userProfile = OAuthAttributes.extract(registrationId, attributes);

        Member member = updateOrSaveUser(userProfile);

        // member.getRoles() 에 담긴 모든 ROLE_* 을 Authority 로 변환
        Set<SimpleGrantedAuthority> authorities = member.getRoles().stream()
                .map(Role::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        Map<String, Object> customAttribute =
                getCustomAttribute(registrationId, userNameAttributeName, attributes, userProfile);

        return new DefaultOAuth2User(
                authorities,
                customAttribute,
                userNameAttributeName
        );
    }

    public Map getCustomAttribute(String registrationId,
                                  String userNameAttributeName,
                                  Map<String, Object> attributes,
                                  MemberProfile userProfile) {
        Map<String, Object> customAttribute = new ConcurrentHashMap<>();

        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", userProfile.getName());
        customAttribute.put("providerId", userProfile.getProviderId());

        return customAttribute;
    }

    public Member updateOrSaveUser(MemberProfile memberProfile) {

        Member member = memberRepository
                .findByProviderAndProviderId(memberProfile.getProvider(), memberProfile.getProviderId())
                .map(value -> value.updateMember(memberProfile.getName()))
                // 신규 유저일 경우
                .orElseGet(() -> {
                    Member m = memberProfile.toEntity();
                    m.addRole(Role.ROLE_USER);  // 기본 유저 권한 부여
                    return m;
                });

        return memberRepository.save(member);
    }

}