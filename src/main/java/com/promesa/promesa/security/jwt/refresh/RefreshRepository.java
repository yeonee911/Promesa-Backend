package com.promesa.promesa.security.jwt.refresh;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // Redis에 저장: key = "RT:" + refreshToken, value = nickname
    public void save(String refreshToken, String nickname, long expirationMs) {
        redisTemplate.opsForValue().set("RT:" + refreshToken, nickname, expirationMs, TimeUnit.MILLISECONDS);
    }

    // Redis에서 nickname 조회: key = "RT:" + refreshToken
    public String findByToken(String refreshToken) {
        return redisTemplate.opsForValue().get("RT:" + refreshToken);
    }

    // Redis에서 해당 refreshToken 삭제
    public void delete(String refreshToken) {
        redisTemplate.delete("RT:" + refreshToken);
    }

    // refreshToken 존재 여부 확인
    public boolean exists(String refreshToken) {
        return redisTemplate.hasKey("RT:" + refreshToken);
    }
}
