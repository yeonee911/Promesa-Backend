package com.promesa.promesa.security.jwt.refresh;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtil {

    public static boolean isLocalRequest(HttpServletRequest request) {
        String state = request.getParameter("state");
        return state != null && (state.contains("localhost") || state.contains("127.0.0.1"));
    }

    public static Cookie createRefreshTokenCookie(String token, long maxAgeMillis, boolean isSecure, boolean includeDomain) {
        Cookie cookie = new Cookie("refresh", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(isSecure);
        cookie.setMaxAge((int) (maxAgeMillis / 1000));
        if (includeDomain) {
            cookie.setDomain(".promesa.co.kr");
        }
        return cookie;
    }

    public static Cookie expireRefreshTokenCookie(boolean isSecure, boolean includeDomain) {
        Cookie cookie = new Cookie("refresh", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(isSecure);
        cookie.setMaxAge(0);
        if (includeDomain) {
            cookie.setDomain(".promesa.co.kr");
        }
        return cookie;
    }
}

