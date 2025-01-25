package com.antique.service.user;

import com.antique.domain.User;
import com.antique.dto.user.UserRequestDTO;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import com.antique.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long updateUserNickname(Long userId, String nickname) {
        // 1. 기존 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(CommonErrorCode.USER_NOT_FOUND));

        // 2. 닉네임 업데이트
        user.updateNickname(nickname);

        // 3. 저장 및 반환
        userRepository.save(user);
        return user.getUserId(); // 업데이트된 유저 ID 반환
    }

    // 닉네임 중복 확인 메서드
    @Transactional(readOnly = true)
    public boolean nicknameCheck(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
