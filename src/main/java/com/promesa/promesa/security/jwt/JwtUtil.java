package com.promesa.promesa.security.jwt;

import com.promesa.promesa.security.jwt.exception.ExpiredJwtException;
import com.promesa.promesa.security.jwt.exception.InvalidJwtException;
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
        System.out.println("🔥 JWT Secret: " + jwtProperties.getSecret());
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }
    public String createToken(String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw e; // 반드시 throw 하도록 설정되어 있어야 위 catch 문에서 분기 가능
        }
    }


    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
