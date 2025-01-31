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
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "해당 닉네임은 이미 사용 중입니다."),
    // Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    NO_PRODUCT_BY_SEARCH(HttpStatus.NOT_FOUND, "검색명에 맞는 상품이 없습니다."),
    // Review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자의 리뷰가 존재하지 않습니다."),
    REVIEW_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "해당 리뷰가 존재하지 않습니다."),
    // 기타
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    GOOGLE_TOKEN_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "Google Access Token 요청에 실패했습니다."),
    GOOGLE_REDIRECT_FAILED(HttpStatus.BAD_REQUEST, "Google 리다이렉트 요청에 실패했습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "Refresh Token이 일치하지 않습니다.");


    private final HttpStatus status;
    private final String message;

}
