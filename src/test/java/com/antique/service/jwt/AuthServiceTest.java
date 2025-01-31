package com.antique.service.jwt;

import com.antique.dto.login.jwt.AccessTokenDTO;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService; // 테스트할 객체

    @Mock
    private JwtTokenGenerator jwtTokenGenerator;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Test
    void testRefreshAccessToken_Success() {
        // Given
        String validRefreshToken = "valid_refresh_token";
        Long userId = 1L;
        String newAccessToken = "new_access_token";

        when(jwtTokenGenerator.extractUserId(validRefreshToken)).thenReturn(userId);
        when(refreshTokenService.getRefreshToken(userId)).thenReturn(validRefreshToken);
        when(jwtTokenGenerator.generateAccessToken(userId)).thenReturn(newAccessToken);

        // When
        AccessTokenDTO result = authService.refreshAccessToken(validRefreshToken);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Access Token 재발급 완료");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getAccessToken()).isEqualTo(newAccessToken);

        // 호출 검증
        verify(jwtTokenGenerator, times(1)).extractUserId(validRefreshToken);
        verify(refreshTokenService, times(1)).getRefreshToken(userId);
        verify(jwtTokenGenerator, times(1)).generateAccessToken(userId);
    }

    @Test
    void testRefreshAccessToken_InvalidToken() {
        // Given: null 또는 빈 Refresh Token
        String invalidRefreshToken = "";

        // When & Then
        assertThatThrownBy(() -> authService.refreshAccessToken(invalidRefreshToken))
                .isInstanceOf(BaseException.class)
                .hasMessage(CommonErrorCode.INVALID_TOKEN.getMessage());

        // jwtTokenGenerator, refreshTokenService 호출되지 않음
        verify(jwtTokenGenerator, never()).extractUserId(anyString());
        verify(refreshTokenService, never()).getRefreshToken(anyLong());
        verify(jwtTokenGenerator, never()).generateAccessToken(anyLong());
    }

    @Test
    void testRefreshAccessToken_RefreshTokenMismatch() {
        // Given
        String providedRefreshToken = "old-token";
        String storedRefreshToken = "different-token"; // 다른 값
        Long userId = 1L;

        when(jwtTokenGenerator.extractUserId(providedRefreshToken)).thenReturn(userId);
        when(refreshTokenService.getRefreshToken(userId)).thenReturn(storedRefreshToken);

        // When & Then
        assertThatThrownBy(() -> authService.refreshAccessToken(providedRefreshToken))
                .isInstanceOf(BaseException.class)
                .hasMessage(CommonErrorCode.REFRESH_TOKEN_MISMATCH.getMessage());

        // 호출 검증
        verify(jwtTokenGenerator, times(1)).extractUserId(providedRefreshToken);
        verify(refreshTokenService, times(1)).getRefreshToken(userId);
        verify(jwtTokenGenerator, never()).generateAccessToken(anyLong()); // Access Token 생성되지 않아야 함
    }
}