package com.antique.service;

import com.antique.domain.User;
import com.antique.dto.user.UserRequestDTO;
import com.antique.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService; // 테스트할 객체

    @Mock
    private UserRepository userRepository;


    @Test
    void testUpdateUserDetails() {
        // Given: 기존 사용자 Mock 데이터
        User user = User.builder()
                .userId(1L)
                .email("test@example.com")
                .nickname("OldNickname")
                .address("Old Address")
                .build();

        UserRequestDTO userRequestDto = UserRequestDTO.builder()
                .nickname("UpdatedNickname")
                .address("Updated Address")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When: UserService 호출
        Long updatedUserId = userService.updateUserDetails(1L, userRequestDto);

        // Then: 검증
        assertThat(updatedUserId).isEqualTo(1L);

        // 사용자 필드가 업데이트되었는지 확인
        assertThat(user.getNickname()).isEqualTo("UpdatedNickname");
        assertThat(user.getAddress()).isEqualTo("Updated Address");
    }

    @Test
    void testUpdateUserDetails_UseridNotExist() {
        // Given: 존재하지 않는 userId 설정
        Long notExistUserId = 999L;

        UserRequestDTO userRequestDto = UserRequestDTO.builder()
                .nickname("NewNickname")
                .address("New Address")
                .build();

        when(userRepository.findById(notExistUserId)).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUserDetails(notExistUserId, userRequestDto)
        );

        assertEquals("999번 userid의 사용자를 찾을 수 없습니다: ", exception.getMessage());
    }

    @Test
    void testUpdateUserNickname() {
        // Given: 기존 사용자 Mock 데이터
        User user = User.builder()
                .userId(1L)
                .email("test@example.com")
                .nickname("OldNickname")
                .build();

        String updatedNickname = "NewNickname";

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When: UserService 호출
        Long updatedUserId = userService.updateUserNickname(1L, updatedNickname);

        // Then: 검증
        assertThat(updatedUserId).isEqualTo(1L);

        // 사용자 필드가 업데이트되었는지 확인
        assertThat(user.getNickname()).isEqualTo(updatedNickname);
    }

    @Test
    void testUpdateUserAddress() {
        // Given: 기존 사용자 Mock 데이터
        User user = User.builder()
                .userId(1L)
                .email("test@example.com")
                .address("old address")
                .build();

        String updatedAddress = "new address";

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When: UserService 호출
        Long updatedUserId = userService.updateUserAddress(1L, updatedAddress);

        // Then: 검증
        assertThat(updatedUserId).isEqualTo(1L);

        // 사용자 필드가 업데이트되었는지 확인
        assertThat(user.getAddress()).isEqualTo(updatedAddress);
    }
}