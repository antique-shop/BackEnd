package com.antique.auth.kakao.controller;

import com.antique.auth.kakao.dto.KakaoUserInfoResponseDTO;
import com.antique.auth.kakao.service.KakaoService;
import com.antique.domain.User;
import com.antique.service.jwt.JwtTokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/*
* Redirect된 URL에 전달된 code를 가져오기 위함
*/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class KakaoLoginController {
    private final KakaoService kakaoService;
    private final JwtTokenGenerator jwtTokenGenerator;

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        try {
            // 카카오에서 액세스 토큰을 가져옵니다.
            String accessToken = kakaoService.getAccessTokenFromKakao(code);

            // 액세스 토큰을 사용하여 사용자 정보를 가져옵니다.
            KakaoUserInfoResponseDTO userInfo = kakaoService.getUserInfo(accessToken);

            // 사용자 정보를 기반으로 등록 또는 로그인합니다.
            User user = kakaoService.registerOrLoginUser(userInfo);

            // JWT 생성
            String jwtToken = jwtTokenGenerator.generateAccessToken(user.getUserId());

            // 로그인 성공 시 사용자 정보와 JWT를 반환합니다.
            return new ResponseEntity<>(new LoginResponse(user, jwtToken), HttpStatus.OK);
        } catch (Exception e) {
            // 예외 발생 시 적절한 에러 메시지를 반환합니다.
            return new ResponseEntity<>("Login failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 로그인 응답 DTO
    public static class LoginResponse {
        private User user;
        private String token;

        public LoginResponse(User user, String token) {
            this.user = user;
            this.token = token;
        }

        public User getUser() {
            return user;
        }

        public String getToken() {
            return token;
        }
    }
}
