package com.antique.service.user;

import com.antique.TestDataFactory;
import com.antique.domain.User;
import com.antique.dto.user.UserRequestDTO;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
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
    void testUpdateUserNickname() {
        // Given: 기존 사용자 Mock 데이터
        User user = TestDataFactory.createUser(1L, "test@example.com", "OldNickname");

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
}