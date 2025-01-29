package com.antique.controller.user;

import com.antique.dto.login.google.GoogleAccountProfileResponse;
import com.antique.dto.user.UserResponseDTO;
import com.antique.service.login.google.GoogleClient;
import com.antique.service.login.google.GoogleLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GoogleLoginController {

    private final GoogleClient googleClient;
    private final GoogleLoginService googleLoginService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    /**
     * 사용자가 http://localhost:8080/login/google 요청 시,
     * Google OAuth 로그인 페이지로 자동 리디렉트됨.
     */
    @GetMapping("/login/google")
    public void redirectToGoogleLogin(HttpServletResponse response) {
        String googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=email profile";
        try {
            response.sendRedirect(googleLoginUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 구글 로그인 완료 후, 인증 코드(code)를 받아 로그인 및 회원가입을 처리하는 엔드포인트.
     */
    @GetMapping("/oauth2/callback")
    public ResponseEntity<UserResponseDTO> googleCallback(@RequestParam String code) {
        try {
            // Google API를 통해 사용자 프로필 가져오기
            GoogleAccountProfileResponse googleProfile = googleClient.getGoogleAccountProfile(code);

            // 회원가입 또는 로그인 처리 후 응답 DTO 반환
            UserResponseDTO response = googleLoginService.handleLoginOrRegister(googleProfile);

            // 응답 반환
            return ResponseEntity.status(response.getStatusCode()).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                    new UserResponseDTO(null, "로그인 실패: " + e.getMessage(), 500, null)
            );
        }
    }
}