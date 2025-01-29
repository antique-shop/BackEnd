package com.antique.service.login.google;

import com.antique.domain.User;
import com.antique.dto.login.google.GoogleAccountProfileResponse;
import com.antique.dto.user.UserResponseDTO;
import com.antique.repository.UserRepository;
import com.antique.service.jwt.JwtTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {

    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;

    public UserResponseDTO handleLoginOrRegister(GoogleAccountProfileResponse googleProfile) {
        // 이메일을 기준으로 사용자 검색
        AtomicBoolean isNewUser = new AtomicBoolean(false); // 회원가입 여부를 추적

        User user = userRepository.findByEmail(googleProfile.getEmail())
                .orElseGet(() -> {
                    isNewUser.set(true);
                    return registerNewUser(googleProfile);
                });

        // JWT 발급
        String jwtToken = jwtTokenGenerator.generateToken(user.getUserId().toString());

        // 응답 DTO 생성
        String message = isNewUser.get() ? "회원가입이 완료되었습니다." : "로그인 성공";
        int statusCode = isNewUser.get() ? HttpStatus.CREATED.value() : HttpStatus.OK.value();

        return new UserResponseDTO(null, message, statusCode, jwtToken);
    }

    // 새로운 회원 등록
    private User registerNewUser(GoogleAccountProfileResponse googleProfile) {
        User newUser = User.builder()
                .email(googleProfile.getEmail()) // 이메일 저장
                .grade("basic") // 기본 등급 설정
                .points(0) // 초기 적립금 0
                .transactionCount(0) // 초기 거래 횟수 0
                .rating(0.0f) // 초기 평점 0.0
                .isDeleted(false) // 삭제되지 않은 상태로 설정
                .build();

        return userRepository.save(newUser);
    }
}
