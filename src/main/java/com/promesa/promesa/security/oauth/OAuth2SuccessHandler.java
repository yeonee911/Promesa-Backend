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
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.promesa.promesa.security.jwt.refresh.CookieUtil.*;

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

        // 2. 기존 RefreshToken 제거
        refreshRepository.deleteByNickname(nickname);

        // 3. 새 RefreshToken 저장 (보조 인덱스 포함)
        refreshRepository.save(refreshToken, nickname, jwtProperties.getRefreshTokenExpiration());

        // 4. RefreshToken을 쿠키에 저장
        boolean isSecure = request.isSecure();
        boolean includeDomain = !isLocalRequest(request);
        Cookie refreshCookie = createRefreshTokenCookie(refreshToken, jwtProperties.getRefreshTokenExpiration(), isSecure, includeDomain);
        response.addCookie(refreshCookie);

        log.info("🔐 Set-Cookie: refreshToken set with Secure={}, SameSite=None, Domain={}",
                isSecure, includeDomain ? ".promesa.co.kr" : "(none)");

        // 5. Redirect URI 처리
        String stateParam = request.getParameter("state");
        String baseRedirectUri = "http://localhost:3000";
        String afterLogin = "";

        if (stateParam != null && !stateParam.isBlank()) {
            try {
                URI stateUri = new URI(stateParam);
                baseRedirectUri = stateUri.getScheme() + "://" + stateUri.getHost();
                if (stateUri.getPort() != -1) {
                    baseRedirectUri += ":" + stateUri.getPort();
                }

                // afterLogin 파라미터 추출
                String query = stateUri.getQuery();
                if (query != null) {
                    for (String param : query.split("&")) {
                        String[] keyValue = param.split("=");
                        if (keyValue.length == 2 && keyValue[0].equals("afterLogin")) {
                            afterLogin = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                            break;
                        }
                    }
                }
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("리다이렉트 URI 파싱 실패: " + stateParam);
            }
        }

        // 6. accessToken & afterLogin redirect 전달
        String finalRedirect = baseRedirectUri + "/login/success"
                + "?accessToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8);

        if (!afterLogin.isBlank()) {
            finalRedirect += "&afterLogin=" + URLEncoder.encode(afterLogin, StandardCharsets.UTF_8);
        }

        log.info("OAuth2 Login Success: {}", nickname);
        log.info("AccessToken & RefreshToken issued, redirecting to {}", finalRedirect);

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
