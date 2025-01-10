package com.antique.exception;

import com.antique.dto.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<UserResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        UserResponseDto responseDto = new UserResponseDto(
                null, // 실패 시 userId를 포함하지 않음
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(responseDto);
    }

    // 그 외 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserResponseDto> handleGenericException(Exception ex) {
        UserResponseDto responseDto = new UserResponseDto(
                null, // 실패 시 userId를 포함하지 않음
                "서버 내부 오류가 발생했습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }
}
