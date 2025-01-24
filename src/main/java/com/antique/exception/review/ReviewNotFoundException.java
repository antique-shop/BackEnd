package com.antique.exception.review;

import lombok.Getter;

@Getter
public class ReviewNotFoundException extends RuntimeException {
    private final ReviewErrorCode errorCode;

    public ReviewNotFoundException(ReviewErrorCode reviewErrorCode) {
        this.errorCode = reviewErrorCode;
    }

    public ReviewErrorCode getErrorCode() {
        return errorCode;
    }
}
