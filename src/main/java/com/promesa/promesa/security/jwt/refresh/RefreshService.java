package com.promesa.promesa.security.jwt.refresh;

import com.promesa.promesa.common.dto.SuccessResponse;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.domain.Role;
import com.promesa.promesa.domain.member.exception.MemberNotFoundException;
import com.promesa.promesa.security.jwt.JwtProperties;
import com.promesa.promesa.security.jwt.JwtUtil;
import com.promesa.promesa.security.jwt.exception.ExpiredRefreshTokenException;
import com.promesa.promesa.security.jwt.exception.InvalidRefreshTokenException;
import com.promesa.promesa.security.jwt.exception.MissingRefreshTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;

    public SuccessResponse<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        // 1. refreshToken 추출 (쿼리 → 쿠키 순서)
        String refreshToken = extractRefreshToken(request);
        if (refreshToken == null || refreshToken.isBlank()) {
            throw MissingRefreshTokenException.EXCEPTION;
        }

        // 2. 유효성 검사
        if (jwtUtil.isExpired(refreshToken)) {
            refreshRepository.delete(refreshToken);
            throw ExpiredRefreshTokenException.EXCEPTION;
        }

        if (!"refresh".equals(jwtUtil.getCategory(refreshToken))) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        String nicknameFromToken = jwtUtil.getNickname(refreshToken);
        if (!refreshRepository.exists(refreshToken)) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        String nicknameInRedis = refreshRepository.findByToken(refreshToken);
        if (!nicknameFromToken.equals(nicknameInRedis)) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        // 3) DB에서 Member 로드 & roles 리스트 생성
        String[] parts = nicknameFromToken.split(":", 2);
        Member member = memberRepository.findByProviderAndProviderId(parts[0], parts[1])
                .orElseThrow(()-> MemberNotFoundException.EXCEPTION);
        List<String> roles = member.getRoles().stream()
                .map(Role::name)
                .toList();

        // 3. 새 토큰 발급
        String newAccessToken = jwtUtil.createAccessToken(nicknameFromToken, roles);
        String newRefreshToken = jwtUtil.createRefreshToken(nicknameFromToken, roles);

        refreshRepository.delete(refreshToken);
        refreshRepository.save(newRefreshToken, nicknameFromToken, jwtProperties.getRefreshTokenExpiration());

        // 4. RefreshToken을 쿠키에 저장
        boolean isSecure = request.isSecure();
        boolean includeDomain = !CookieUtil.isLocalRequest(request);
        String setCookieHeader = CookieUtil.buildSetCookieHeader(newRefreshToken, jwtProperties.getRefreshTokenExpiration(), isSecure, includeDomain);
        response.setHeader("Set-Cookie", setCookieHeader);

        // 5. 응답 JSON에 AccessToken 포함
        return SuccessResponse.success(200, Map.of(
                "accessToken", newAccessToken
        ));
    }

    private String extractRefreshToken(HttpServletRequest request) {
        // 쿼리 파라미터 우선
        String param = request.getParameter("refreshToken");
        if (param != null && !param.isBlank()) {
            return param;
        }

        // 쿠키에서 찾기
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    private void setRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response, String token) {
        boolean isSecureRequest = request.isSecure();

        StringBuilder cookieBuilder = new StringBuilder();
        cookieBuilder.append("refresh=").append(token)
                .append("; Path=/")
                .append("; Max-Age=").append(jwtProperties.getRefreshTokenExpiration() / 1000)
                .append("; HttpOnly");

        if (isSecureRequest) {
            cookieBuilder.append("; Secure");
            cookieBuilder.append("; SameSite=None");
        }

        response.setHeader("Set-Cookie", cookieBuilder.toString());
    }
}
