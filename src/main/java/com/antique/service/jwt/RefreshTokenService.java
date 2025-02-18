package com.antique.service.jwt;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;


    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    private String getKey(Long userId) {
        return "refreshToken:" + userId;
    }

    /**
     * Refresh Token 저장 (Redis)
     */
    public void saveRefreshToken(Long userId, String refreshToken) {
        String key = "refreshToken:" + userId;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(refreshTokenExpiration));
    }

    /**
     * Refresh Token 조회 (Redis)
     */
    public String getRefreshToken(Long userId) {
        String token = redisTemplate.opsForValue().get(getKey(userId));
        if (token == null) {
            throw new RuntimeException("Refresh Token not found or expired for userId: " + userId);
        }
        return token;
    }

    /**
     * Refresh Token 삭제 (로그아웃 시)
     */
    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(getKey(userId));
    }
}
