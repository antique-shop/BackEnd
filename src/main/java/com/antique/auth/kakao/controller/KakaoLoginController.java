package com.antique.auth.kakao.controller;

import com.antique.auth.kakao.dto.KakaoUserInfoResponseDTO;
import com.antique.auth.kakao.service.KakaoService;
import com.antique.domain.User;
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
@RequestMapping("")
public class KakaoLoginController {
    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) throws IOException {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoUserInfoResponseDTO userInfo = kakaoService.getUserInfo(accessToken);

        User user = kakaoService.registerOrLoginUser(userInfo);

        // 여기에 서버 사용자 로그인(인증) 또는 회원가입 로직 추가
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
