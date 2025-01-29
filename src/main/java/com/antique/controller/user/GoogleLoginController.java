package com.antique.controller.user;

import com.antique.dto.login.google.GoogleAccountProfileResponse;
import com.antique.dto.login.google.GoogleLoginDTO;
import com.antique.dto.user.UserResponseDTO;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import com.antique.service.login.google.GoogleClient;
import com.antique.service.login.google.GoogleLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "OAuth 로그인 API", description = "Google OAuth 로그인 API")
public class GoogleLoginController {

    private final GoogleClient googleClient;
    private final GoogleLoginService googleLoginService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    /**
     * Google 로그인 페이지로 리디렉트
     */
    @Operation(summary = "Google 로그인 페이지로 이동", description = "사용자를 Google OAuth 로그인 페이지로 리디렉트합니다.")
    @GetMapping("/login/google")
    public void GoogleLogin(HttpServletResponse response) {
        String googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=email profile";
        try {
            response.sendRedirect(googleLoginUrl);
        } catch (IOException e) {
            throw new BaseException(CommonErrorCode.GOOGLE_REDIRECT_FAILED);
        }
    }

    /**
     * Google OAuth 로그인 처리
     */
    @Operation(summary = "Google OAuth 로그인", description = "Google 인증 후 받은 code를 통해 로그인 또는 회원가입을 처리합니다.")
    @GetMapping("/oauth2/callback")
    public ResponseEntity<GoogleLoginDTO> handleGoogleLogin(@RequestParam String code) {
        GoogleAccountProfileResponse googleProfile = googleClient.getGoogleAccountProfile(code);
        GoogleLoginDTO response = googleLoginService.loginOrRegisterUser(googleProfile);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}