package com.antique;

import com.antique.domain.User;
import com.antique.dto.user.UserRequestDTO;


public class TestDataFactory {
    public static User createUser(Long userId, String email, String nickname, String address) {
        return User.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .address(address)
                .build();
    }

    public static UserRequestDTO createUserRequestDTO(String nickname, String address) {
        return UserRequestDTO.builder()
                .nickname(nickname)
                .address(address)
                .build();
    }
}