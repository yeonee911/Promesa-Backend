package com.promesa.promesa.security.oauth;

import com.promesa.promesa.security.jwt.JwtProperties;
import com.promesa.promesa.security.jwt.JwtUtil;
import com.promesa.promesa.security.jwt.refresh.RefreshRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
import java.net.URI;
import java.net.URISyntaxException;
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

        // 3. Refresh Token을 HttpOnly 쿠키에 저장 (state 기준 Secure 여부 설정)
        String stateParam = request.getParameter("state");
        boolean isLocalState = stateParam != null && (
                stateParam.contains("localhost") || stateParam.contains("127.0.0.1")
        );

        Cookie refreshCookie = new Cookie("refresh", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(!isLocalState); // ← 핵심: state 기준으로 Secure 설정
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int)(jwtProperties.getRefreshTokenExpiration() / 1000));
        response.addCookie(refreshCookie);

        // 4. AccessToken은 쿼리 파라미터로만 전달 (쿠키 저장 X)
        String baseRedirectUri = (stateParam == null || stateParam.isBlank())
                ? "http://localhost:3000"
                : stateParam;

        // 5. 보안상 허용된 base 주소만 허용
        try {
            URI uri = new URI(baseRedirectUri);
            String host = uri.getHost();

            if (!(host.equals("localhost") || host.equals("ceos-promesa.vercel.app") || host.equals("promesa.co.kr"))) {
                throw new IllegalArgumentException("허용되지 않은 리다이렉트 base URI: " + baseRedirectUri);
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("리다이렉트 URI 파싱 실패: " + baseRedirectUri);
        }

        // 6. base URI에 무조건 /login/success 붙여서 accessToken 전달
        String finalRedirect = baseRedirectUri + "/login/success?accessToken=" + accessToken;

        log.info("✅ OAuth2 Login Success: {}", nickname);
        log.info("🔑 AccessToken issued, redirecting to {}", finalRedirect);

        response.sendRedirect(finalRedirect);
    }

    private String extractProviderId(String provider, Map<String, Object> attributes) {
        if ("kakao".equals(provider)) {
            return String.valueOf(attributes.get("id")); // 카카오의 고유 ID는 "id" 필드
        }
        throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
    }
}
