package com.antique.exception.user;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final UserErrorCode errorCode;

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND.getMessage());
        this.errorCode = UserErrorCode.USER_NOT_FOUND;
    }

    public UserNotFoundException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}