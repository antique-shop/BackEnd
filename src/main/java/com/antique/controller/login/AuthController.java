package com.antique.controller.login;

import com.antique.dto.login.jwt.AccessTokenDTO;
import com.antique.service.jwt.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "인증 API", description = "JWT 인증 관련 API")
public class AuthController {

    private final AuthService authService;

    /**
     * Refresh Token을 이용한 Access Token 재발급
     */
    @Operation(summary = "Access Token 재발급", description = "만료된 Access Token을 Refresh Token을 이용해 재발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenDTO> refreshAccessToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.refreshAccessToken(refreshToken));
    }
}
