package com.antique.exception.dibs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum DibsErrorCode {
    DIBS_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 찜한 상품입니다."), // 중복 찜 등록 예외
    DIBS_NOT_FOUND(HttpStatus.BAD_REQUEST, "요청된 사용자가 찜한 상품이 아닙니다.");

    private final HttpStatus status;
    private final String message;
}