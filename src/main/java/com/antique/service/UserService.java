package com.antique.service;

import com.antique.domain.User;
import com.antique.dto.UserRequestDto;
import com.antique.exception.user.UserNotFoundException;
import com.antique.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long updateUserDetails(Long userId, UserRequestDto userRequestDto) {
        // 1. 기존 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 2. 닉네임과 주소 업데이트
        user.updateNicknameAndAddress(userRequestDto.getNickname(), userRequestDto.getAddress());

        // 3. 저장 및 반환
        userRepository.save(user);
        return user.getUserId(); // 업데이트된 유저 ID 반환
    }
}
