package com.promesa.promesa.security.oauth;

import com.promesa.promesa.security.jwt.JwtProperties;
import com.promesa.promesa.security.jwt.JwtUtil;
import com.promesa.promesa.security.jwt.refresh.RefreshRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) oauthToken.getPrincipal();

        String provider = oauthToken.getAuthorizedClientRegistrationId(); // ex. "kakao"
        String providerId = extractProviderId(provider, oAuth2User.getAttributes());

        String nickname = provider + ":" + providerId;
        String role = "USER";

        // 1. JWT 발급
        String accessToken = jwtUtil.createAccessToken(nickname, role);
        String refreshToken = jwtUtil.createRefreshToken(nickname, role);

        // 2. Redis에 Refresh Token 저장
        refreshRepository.save(refreshToken, nickname, jwtProperties.getRefreshTokenExpiration());

        // 3. 쿠키에도 Refresh Token 저장 (보안 테스트 시 HttpOnly + Secure 설정)
        StringBuilder cookieBuilder = new StringBuilder();
        cookieBuilder.append("refresh=").append(refreshToken)
                .append("; Path=/")
                .append("; Max-Age=").append(jwtProperties.getRefreshTokenExpiration() / 1000)
                .append("; HttpOnly");

        // HTTPS 환경일 경우 SameSite=None + Secure
        if (!isLocalRequest(request)) {
            cookieBuilder.append("; Secure");
            cookieBuilder.append("; SameSite=None");
        }

        response.setHeader("Set-Cookie", cookieBuilder.toString());

        // 4. redirect URI 추출
        String stateParam = request.getParameter("state");
        String baseRedirectUri = (stateParam == null || stateParam.isBlank())
                ? "http://localhost:3000"
                : stateParam;

        try {
            URI uri = new URI(baseRedirectUri);
            String host = uri.getHost();

            if (!(host.equals("localhost") || host.equals("ceos-promesa.vercel.app") || host.equals("promesa.co.kr"))) {
                throw new IllegalArgumentException("허용되지 않은 리다이렉트 base URI: " + baseRedirectUri);
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("리다이렉트 URI 파싱 실패: " + baseRedirectUri);
        }

        // 5. 쿼리 파라미터로 AccessToken + RefreshToken 전달 (개발 중만 사용)
        String finalRedirect = baseRedirectUri + "/login/success"
                + "?accessToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
                + "&refresh=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);

        /*
        // 🔒 운영 전환 시: 쿼리 파라미터에 refreshToken을 넘기지 않음
        String finalRedirect = baseRedirectUri + "/login/success"
                + "?accessToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8);
        */

        log.info("✅ OAuth2 Login Success: {}", nickname);
        log.info("🔑 AccessToken & RefreshToken issued, redirecting to {}", finalRedirect);

        response.sendRedirect(finalRedirect);
    }

    private String extractProviderId(String provider, Map<String, Object> attributes) {
        if ("kakao".equals(provider)) {
            return String.valueOf(attributes.get("id")); // 카카오의 고유 ID는 "id" 필드
        }
        throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
    }

    private boolean isLocalRequest(HttpServletRequest request) {
        String state = request.getParameter("state");
        return state != null && (state.contains("localhost") || state.contains("127.0.0.1"));
    }
}
