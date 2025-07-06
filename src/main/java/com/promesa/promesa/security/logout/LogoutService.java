package com.promesa.promesa.security.logout;

import com.promesa.promesa.common.dto.SuccessResponse;
import com.promesa.promesa.security.jwt.JwtUtil;
import com.promesa.promesa.security.jwt.refresh.RefreshRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public SuccessResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;

        // null-safe 쿠키 접근
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // 쿠키에 Refresh 토큰이 아예 없을 경우
        if (refreshToken == null) {
            return SuccessResponse.success(200, "로그아웃되었습니다");
        }

        // 쿠키에 Refresh 토큰이 있지만, 유효하지 않거나 파싱 실패한 경우
        String nickname;
        try {
            nickname = jwtUtil.getNickname(refreshToken); // 닉네임 파싱(검증만)
        } catch (Exception e) {
            return SuccessResponse.success(200, "이미 로그아웃되었거나 유효하지 않은 토큰입니다");
        }

        // Redis에 해당 refreshToken이 존재하는지 확인
        if (!refreshRepository.exists(refreshToken)) {
            return SuccessResponse.success(200, "이미 로그아웃된 상태입니다");
        }

        // Redis에서 해당 refreshToken 삭제
        refreshRepository.delete(refreshToken);

        // 쿠키 무효화
        Cookie expiredCookie = new Cookie("refresh", null);
        expiredCookie.setMaxAge(0);
        expiredCookie.setHttpOnly(true);
        expiredCookie.setPath("/");
        expiredCookie.setSecure(true); // if using HTTPS

        response.addCookie(expiredCookie);

        return SuccessResponse.success(200, "로그아웃되었습니다");
    }

}