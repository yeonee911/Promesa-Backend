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
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        System.out.println("🔥 JWT Secret: " + jwtProperties.getSecret());
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // ✅ 공통 JWT 생성 메서드
    public String createJwt(String category, String nickname, List<String> roles, Long expiredMs) {
        Claims claims = Jwts.claims();
        claims.put("category", category); // access or refresh
        claims.put("nickname", nickname);
        claims.put("role", roles);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiredMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // ✅ Access / Refresh 생성
    public String createAccessToken(String nickname, List<String> roles) {
        return createJwt("access", nickname, roles, jwtProperties.getAccessTokenExpiration());
    }

    public String createRefreshToken(String nickname, List<String> roles) {
        return createJwt("refresh", nickname, roles, jwtProperties.getRefreshTokenExpiration());
    }

    // ✅ 유효성 검사 (예외 발생 방식)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw e; // JwtAuthenticationFilter 등에서 캐치
        }
    }

    // ✅ 만료 여부 판단용
    public boolean isExpired(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return false;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            throw InvalidJwtException.EXCEPTION;
        }
    }

    // ✅ Subject 조회 (subject 기반 로직 호환용)
    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ Claims 추출 메서드
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

    // ✅ Claim 단건 추출
    public String getCategory(String token) {
        return getAllClaims(token).get("category", String.class);
    }

    public String getNickname(String token) {
        return getAllClaims(token).get("nickname", String.class);
    }

    public String getRole(String token) {
        return getAllClaims(token).get("role", String.class);
    }
}
