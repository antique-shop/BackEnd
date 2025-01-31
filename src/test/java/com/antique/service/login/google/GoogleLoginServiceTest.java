package com.antique.service.login.google;

import com.antique.TestDataFactory;
import com.antique.domain.User;
import com.antique.dto.login.google.GoogleAccountProfileResponse;
import com.antique.dto.login.google.GoogleLoginDTO;
import com.antique.dto.user.UserResponseDTO;
import com.antique.repository.UserRepository;
import com.antique.service.jwt.JwtTokenGenerator;
import com.antique.service.jwt.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 사용
class GoogleLoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenGenerator jwtTokenGenerator;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private GoogleLoginService googleLoginService;

    private GoogleAccountProfileResponse googleProfile;
    private User existingUser;
    private User newUser;

    @BeforeEach
    void setUp() {
        // Google 계정 정보 생성 (TestDataFactory 활용)
        googleProfile = new GoogleAccountProfileResponse();
        googleProfile.setEmail("testuser@gmail.com");

        // 기존 사용자 및 새로운 사용자 생성 (TestDataFactory 활용)
        existingUser = TestDataFactory.createUser(1L, "testuser@gmail.com", "TestUser");
        newUser = TestDataFactory.createUser(2L, "newuser@gmail.com", "NewUser");
    }

    @Test
    void 로그인_성공_기존_사용자() {
        // given
        when(userRepository.findByEmail(googleProfile.getEmail())).thenReturn(Optional.of(existingUser));
        when(jwtTokenGenerator.generateAccessToken(existingUser.getUserId())).thenReturn("mocked-access-token");
        when(jwtTokenGenerator.generateRefreshToken(existingUser.getUserId())).thenReturn("mocked-refresh-token");

        // Refresh Token 저장 동작 Mock 설정
        doNothing().when(refreshTokenService).saveRefreshToken(existingUser.getUserId(), "mocked-refresh-token");

        // when
        GoogleLoginDTO response = googleLoginService.loginOrRegisterUser(googleProfile);

        // then
        assertThat(response.getMessage()).isEqualTo("로그인 성공");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getAccessToken()).isEqualTo("mocked-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("mocked-refresh-token");

        verify(userRepository, times(1)).findByEmail(googleProfile.getEmail());
        verify(jwtTokenGenerator, times(1)).generateAccessToken(existingUser.getUserId());
        verify(jwtTokenGenerator, times(1)).generateRefreshToken(existingUser.getUserId());
        verify(refreshTokenService, times(1)).saveRefreshToken(existingUser.getUserId(), "mocked-refresh-token");
    }

    @Test
    void 회원가입_새로운_사용자() {
        // given
        GoogleAccountProfileResponse newGoogleProfile = new GoogleAccountProfileResponse();
        newGoogleProfile.setEmail("newuser@gmail.com");

        when(userRepository.findByEmail(newGoogleProfile.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(jwtTokenGenerator.generateAccessToken(newUser.getUserId())).thenReturn("mocked-access-token");
        when(jwtTokenGenerator.generateRefreshToken(newUser.getUserId())).thenReturn("mocked-refresh-token");

        // Refresh Token 저장 동작 Mock 설정
        doNothing().when(refreshTokenService).saveRefreshToken(newUser.getUserId(), "mocked-refresh-token");

        // when
        GoogleLoginDTO response = googleLoginService.loginOrRegisterUser(newGoogleProfile);

        // then
        assertThat(response.getMessage()).isEqualTo("회원가입이 완료되었습니다.");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getAccessToken()).isEqualTo("mocked-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("mocked-refresh-token");

        verify(userRepository, times(1)).findByEmail(newGoogleProfile.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtTokenGenerator, times(1)).generateAccessToken(newUser.getUserId());
        verify(jwtTokenGenerator, times(1)).generateRefreshToken(newUser.getUserId());
        verify(refreshTokenService, times(1)).saveRefreshToken(newUser.getUserId(), "mocked-refresh-token");
    }
}