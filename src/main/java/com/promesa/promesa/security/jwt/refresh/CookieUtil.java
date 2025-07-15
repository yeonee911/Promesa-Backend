package com.promesa.promesa.security.jwt.refresh;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtil {

    public static boolean isLocalRequest(HttpServletRequest request) {
        String state = request.getParameter("state");
        return state != null && (state.contains("localhost") || state.contains("127.0.0.1"));
    }

    public static String buildSetCookieHeader(String token, long maxAgeMillis, boolean isSecure, boolean includeDomain) {
        StringBuilder cookieBuilder = new StringBuilder();
        cookieBuilder.append("refresh=").append(token)
                .append("; Path=/")
                .append("; Max-Age=").append(maxAgeMillis / 1000)
                .append("; HttpOnly");

        if (isSecure) {
            cookieBuilder.append("; Secure");
            cookieBuilder.append("; SameSite=None");
        }

        if (includeDomain) {
            cookieBuilder.append("; Domain=.promesa.co.kr");
        }

        return cookieBuilder.toString();
    }

    public static String buildExpiredSetCookieHeader(boolean isSecure, boolean includeDomain) {
        StringBuilder cookieBuilder = new StringBuilder();
        cookieBuilder.append("refresh=; Path=/")
                .append("; Max-Age=0")
                .append("; HttpOnly");

        if (isSecure) {
            cookieBuilder.append("; Secure");
            cookieBuilder.append("; SameSite=None");
        }

        if (includeDomain) {
            cookieBuilder.append("; Domain=.promesa.co.kr");
        }

        return cookieBuilder.toString();
    }
}
