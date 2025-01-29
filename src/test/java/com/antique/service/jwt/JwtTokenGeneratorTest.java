package com.antique.service.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenGeneratorTest {

    @InjectMocks
    private JwtTokenGenerator jwtTokenGenerator;

    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        // 테스트 환경에서 값을 직접 주입
        ReflectionTestUtils.setField(jwtTokenGenerator, "secretKey", "dTY5T0YwQm9pbk56dExkTVVHOWhJbVpUWW5HT1I1Ukxq");
        ReflectionTestUtils.setField(jwtTokenGenerator, "accessExpirationTime", 3600000L); // 1시간
        ReflectionTestUtils.setField(jwtTokenGenerator, "refreshExpirationTime", 1209600000L); // 14일
    }

    @Test
    void AccessToken_생성() {
        // when
        String token = jwtTokenGenerator.generateAccessToken(userId);

        // then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void RefreshToken_생성() {
        // when
        String token = jwtTokenGenerator.generateRefreshToken(userId);

        // then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void AccessToken에서_userId_추출() {
        // given
        String token = jwtTokenGenerator.generateAccessToken(userId);

        // when
        Long extractedUserId = jwtTokenGenerator.extractUserId(token);

        // then
        assertThat(extractedUserId).isEqualTo(userId);
    }

    @Test
    void RefreshToken에서_userId_추출() {
        // given
        String token = jwtTokenGenerator.generateRefreshToken(userId);

        // when
        Long extractedUserId = jwtTokenGenerator.extractUserId(token);

        // then
        assertThat(extractedUserId).isEqualTo(userId);
    }

    @Test
    void 유효한_Token을_검증하면_true_반환() {
        // given
        String token = jwtTokenGenerator.generateAccessToken(userId);

        // when
        boolean isValid = jwtTokenGenerator.validateToken(token);

        // then
        assertTrue(isValid);
    }

    @Test
    void 만료된_Token을_검증하면_false_반환() throws InterruptedException {
        // accessExpirationTime을 1ms로 설정하여 즉시 만료되는 토큰 생성
        ReflectionTestUtils.setField(jwtTokenGenerator, "accessExpirationTime", 1L);
        String expiredToken = jwtTokenGenerator.generateAccessToken(userId);

        // 실행 속도 문제 해결을 위해 2ms 대기 후 검증 실행
        Thread.sleep(2);

        // when & then: 만료된 토큰을 검증할 때 예외 발생하는지 확인
        assertThrows(ExpiredJwtException.class, () -> jwtTokenGenerator.validateToken(expiredToken));
    }
}