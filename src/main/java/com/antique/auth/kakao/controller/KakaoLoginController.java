package com.antique.auth.kakao.controller;

import com.antique.auth.kakao.dto.KakaoLoginResponseDTO;
import com.antique.auth.kakao.dto.KakaoUserInfoResponseDTO;
import com.antique.auth.kakao.service.KakaoService;
import com.antique.domain.User;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import com.antique.service.jwt.JwtTokenGenerator;
import com.antique.service.jwt.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/v1/kakaoLogin")
public class KakaoLoginController {
    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    private final KakaoService kakaoService;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenService refreshTokenService;

    /*
     * 카카오 로그인 페이지로 리디렉트
     */
    @Operation(summary = "카카오 로그인 페이지로 이동", description = "사용자를 카카오 OAuth 로그인 페이지로 리디렉트합니다.")
    @GetMapping("/page")
    public void kakaoLogin(HttpServletResponse response) {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + client_id
                + "&redirect_uri=" + redirect_uri;
        try {
            response.sendRedirect(kakaoLoginUrl);
        } catch (IOException e) {
            throw new BaseException(CommonErrorCode.KAKAO_REDIRECT_FAILED);
        }
    }

    /*
     * 카카오 로그인 및 회원가입
     */
    @Operation(summary = "카카오 로그인 및 회원가입", description = "카카오 서버로부터 받은 code를 통해 로그인 및 회원가입을 처리합니다.")
    @GetMapping(value = "/callback", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        try {
            // 카카오에서 액세스 토큰을 가져옴
            String accessToken = kakaoService.getAccessTokenFromKakao(code);

            // 액세스 토큰을 사용하여 사용자 정보를 가져옴
            KakaoUserInfoResponseDTO userInfo = kakaoService.getUserInfo(accessToken);

            // 사용자 정보를 기반으로 등록 또는 로그인
            User user = kakaoService.registerOrLoginUser(userInfo);

            // JWT 생성
            String jwtToken = jwtTokenGenerator.generateAccessToken(user.getUserId());
            String refreshToken = jwtTokenGenerator.generateRefreshToken(user.getUserId()); // 리프레시 토큰 생성

            // Refresh Token을 Redis에 저장
            refreshTokenService.saveRefreshToken(user.getUserId(), refreshToken);

            // 로그인 성공 시 사용자 정보와 JWT를 반환
            return new ResponseEntity<>(new KakaoLoginResponseDTO(user, jwtToken, refreshToken), HttpStatus.OK);
        } catch (Exception e) {
            // 예외 발생 시 적절한 에러 메시지를 반환합니다.
            return new ResponseEntity<>("Login failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
