package com.antique.service.jwt;

import com.antique.dto.login.jwt.AccessTokenDTO;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenService refreshTokenService;

    /**
     * Refresh Token을 이용해 Access Token 재발급
     */
    public AccessTokenDTO refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BaseException(CommonErrorCode.INVALID_TOKEN);
        }

        // Refresh Token에서 userId 추출
        Long userId = jwtTokenGenerator.extractUserId(refreshToken);

        // Redis에서 저장된 Refresh Token 조회
        String storedRefreshToken = refreshTokenService.getRefreshToken(userId);

        // 클라이언트가 보낸 Refresh Token과 Redis에 저장된 Refresh Token 비교
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new BaseException(CommonErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        // 새로운 Access Token 발급
        String newAccessToken = jwtTokenGenerator.generateAccessToken(userId);

        return new AccessTokenDTO("Access Token 재발급 완료", HttpStatus.OK.value(), newAccessToken);
    }
}
