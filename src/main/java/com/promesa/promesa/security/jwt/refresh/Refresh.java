package com.promesa.promesa.security.jwt.refresh;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("refresh")
public class Refresh {
    @Id
    private String nickname;

    private String refresh;
    private String expiration;
}

