package com.antique.service.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private final Long userId = 1L;
    private final String refreshToken = "mocked-refresh-token";

    @BeforeEach
    void setUp() {
        // JWT Refresh Token 만료 시간 설정 (테스트용)
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenExpiration", 1209600000L); // 14일

        // 불필요한 Stubbing 경고를 피하기 위해 lenient() 사용
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void RefreshToken_저장_성공() {
        // given
        String key = "refreshToken:" + userId;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // when
        refreshTokenService.saveRefreshToken(userId, refreshToken);

        // then
        verify(valueOperations, times(1)).set(key, refreshToken, Duration.ofMillis(1209600000L));
    }

    @Test
    void RefreshToken_조회_성공() {
        // given
        String key = "refreshToken:" + userId;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(refreshToken);

        // when
        String retrievedToken = refreshTokenService.getRefreshToken(userId);

        // then
        assertThat(retrievedToken).isEqualTo(refreshToken);
        verify(valueOperations, times(1)).get(key);
    }

    @Test
    void RefreshToken_없을_경우_예외_발생() {
        // given
        String key = "refreshToken:" + userId;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(null);

        // when & then
        assertThrows(RuntimeException.class, () -> refreshTokenService.getRefreshToken(userId));
        verify(valueOperations, times(1)).get(key);
    }

    @Test
    void RefreshToken_삭제_성공() {
        // given
        String key = "refreshToken:" + userId;

        // when
        refreshTokenService.deleteRefreshToken(userId);

        // then
        verify(redisTemplate, times(1)).delete(key);
    }
}