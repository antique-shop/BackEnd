package com.antique.exception.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ReviewErrorCode {
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자의 리뷰가 존재하지 않습니다."),
    REVIEW_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "해당 리뷰가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
