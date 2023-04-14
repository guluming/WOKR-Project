package com.slamdunk.WORK.Redis.Entity;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@RedisHash(value = "RefreshTokenIndex", timeToLive = 30)
public class RefreshToken {
    @Id
    private Long id;
    private String refreshToken;
    private String accessToken;
    private LocalDateTime createdAt;

    public RefreshToken(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.createdAt = LocalDateTime.now();
    }
}
