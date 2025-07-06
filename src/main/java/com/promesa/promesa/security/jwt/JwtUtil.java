package com.promesa.promesa.security.jwt;

import com.promesa.promesa.security.jwt.exception.ExpiredJwtException;
import com.promesa.promesa.security.jwt.exception.InvalidJwtException;
import com.promesa.promesa.security.jwt.exception.InvalidTokenFormatException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // Access 토큰 생성
    public String createAccessToken(String nickname, String role) {
        return createJwt("access", nickname, role, jwtProperties.getAccessTokenExpiration());
    }

    // Refresh 토큰 생성
    public String createRefreshToken(String nickname, String role) {
        return createJwt("refresh", nickname, role, jwtProperties.getRefreshTokenExpiration());
    }

    // 만료 여부 확인
    public boolean isExpired(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return false; // 아직 만료되지 않음
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return true; // 만료됨
        } catch (JwtException e) {
            throw InvalidJwtException.EXCEPTION;
        }
    }

    public String getCategory(String token) {
        return getAllClaims(token).get("category", String.class);
    }

    public String getNickname(String token) {
        return getAllClaims(token).get("nickname", String.class);
    }

    public String getRole(String token) {
        return getAllClaims(token).get("role", String.class);
    }

    private Claims getAllClaims(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw InvalidTokenFormatException.EXCEPTION;
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw ExpiredJwtException.EXCEPTION;
        } catch (JwtException e) {
            throw InvalidJwtException.EXCEPTION;
        }
    }
}
