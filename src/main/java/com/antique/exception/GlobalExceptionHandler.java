package com.antique.exception;

import com.antique.dto.UserResponseDto;
import com.antique.exception.user.UserErrorCode;
import com.antique.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException 처리
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
        UserErrorCode errorCode = ex.getErrorCode();
        UserResponseDto responseDto = new UserResponseDto(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }

    // 그 외 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserResponseDto> handleGenericException(Exception ex) {
        GlobalErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        UserResponseDto responseDto = new UserResponseDto(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }
}
