package com.antique.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    DIBS_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 찜한 상품입니다."), // 중복 찜 등록 예외
    DIBS_NOT_FOUND(HttpStatus.BAD_REQUEST, "요청된 사용자가 찜한 상품이 아닙니다."),
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    // Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    NO_PRODUCT_BY_SEARCH(HttpStatus.NOT_FOUND, "검색명에 맞는 상품이 없습니다."),
    // Review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자의 리뷰가 존재하지 않습니다."),
    REVIEW_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "해당 리뷰가 존재하지 않습니다."),
    // 기타
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");


    private final HttpStatus status;
    private final String message;

}
