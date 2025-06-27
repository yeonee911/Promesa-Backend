package com.promesa.promesa.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promesa.promesa.security.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) oauthToken.getPrincipal();

        String provider = oauthToken.getAuthorizedClientRegistrationId(); // ex. "kakao"
        String providerId = extractProviderId(provider, oAuth2User.getAttributes());

        String subject = provider + ":" + providerId;
        String token = jwtTokenProvider.generateAccessToken(subject);

        // JSON 응답 반환
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    private String extractProviderId(String provider, Map<String, Object> attributes) {
        if ("kakao".equals(provider)) {
            // kakao의 고유 ID는 최상위 "id" 필드에 있음
            return String.valueOf(attributes.get("id"));
        }

        // TODO: 추후 구글, 네이버 등 추가
        throw new IllegalArgumentException("Unsupported provider: " + provider);
    }
}
