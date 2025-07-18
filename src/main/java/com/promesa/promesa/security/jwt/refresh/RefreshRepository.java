package com.promesa.promesa.security.jwt.refresh;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // 저장: RT:refreshToken → nickname, RN:nickname → refreshToken
    public void save(String refreshToken, String nickname, long expirationMs) {
        redisTemplate.opsForValue().set("RT:" + refreshToken, nickname, expirationMs, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set("RN:" + nickname, refreshToken, expirationMs, TimeUnit.MILLISECONDS);
    }

    public String findByToken(String refreshToken) {
        return redisTemplate.opsForValue().get("RT:" + refreshToken);
    }

    public String findByNickname(String nickname) {
        return redisTemplate.opsForValue().get("RN:" + nickname);
    }

    // refreshToken으로 삭제 (양방향 제거)
    public void delete(String refreshToken) {
        String nickname = findByToken(refreshToken);
        if (nickname != null) {
            redisTemplate.delete("RN:" + nickname);
        }
        redisTemplate.delete("RT:" + refreshToken);
    }

    // nickname 기준으로 삭제
    public void deleteByNickname(String nickname) {
        String refreshToken = findByNickname(nickname);
        if (refreshToken != null) {
            redisTemplate.delete("RT:" + refreshToken);
        }
        redisTemplate.delete("RN:" + nickname);
    }

    public boolean exists(String refreshToken) {
        return redisTemplate.hasKey("RT:" + refreshToken);
    }
}
