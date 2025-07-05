package com.promesa.promesa.security.jwt.refresh;

import com.promesa.promesa.common.dto.SuccessResponse;
import com.promesa.promesa.security.jwt.JwtProperties;
import com.promesa.promesa.security.jwt.JwtUtil;
import com.promesa.promesa.security.jwt.exception.ExpiredRefreshTokenException;
import com.promesa.promesa.security.jwt.exception.InvalidRefreshTokenException;
import com.promesa.promesa.security.jwt.exception.MissingRefreshTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final JwtProperties jwtProperties;

    public SuccessResponse<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            throw MissingRefreshTokenException.EXCEPTION;
        }

        if (jwtUtil.isExpired(refreshToken)) {
            refreshRepository.delete(refreshToken); // 만료된 Refresh 토큰 Redis에서도 제거
            throw ExpiredRefreshTokenException.EXCEPTION;
        }

        if (!"refresh".equals(jwtUtil.getCategory(refreshToken))) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        String nicknameFromToken = jwtUtil.getNickname(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // Redis에 해당 refreshToken이 존재하는지 확인
        if (!refreshRepository.exists(refreshToken)) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        // Redis에 저장된 nickname과 JWT 내 nickname 일치 여부 확인
        String nicknameInRedis = refreshRepository.findByToken(refreshToken);
        if (!nicknameFromToken.equals(nicknameInRedis)) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        // 새 토큰 발급
        String newAccessToken = jwtUtil.createAccessToken(nicknameFromToken, role);
        String newRefreshToken = jwtUtil.createRefreshToken(nicknameFromToken, role);

        // Redis 업데이트: 기존 토큰 삭제 + 새 토큰 저장
        refreshRepository.delete(refreshToken);
        refreshRepository.save(newRefreshToken, nicknameFromToken, jwtProperties.getRefreshTokenExpiration());

        // 새 쿠키 설정
        Cookie cookie = createHttpOnlyCookie(request, "refresh", newRefreshToken);
        response.addCookie(cookie);

        return SuccessResponse.success(200, Map.of("accessToken", newAccessToken));
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("refresh".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private Cookie createHttpOnlyCookie(HttpServletRequest request, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure()); // 요청이 HTTPS면 true
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtProperties.getRefreshTokenExpiration() / 1000));
        return cookie;
    }
}
