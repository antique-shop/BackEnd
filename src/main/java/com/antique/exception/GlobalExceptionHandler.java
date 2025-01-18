package com.antique.exception;


import com.antique.exception.product.ProductErrorCode;
import com.antique.exception.product.ProductNotFoundException;
import com.antique.dto.user.UserResponseDTO;
import com.antique.exception.user.UserErrorCode;
import com.antique.exception.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException 처리
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserResponseDTO> handleUserNotFoundException(UserNotFoundException ex) {
        UserErrorCode errorCode = ex.getErrorCode();
        UserResponseDTO responseDto = new UserResponseDTO(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }

    // ProductNotFoundException 처리
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<UserResponseDTO> handleProductNotFoundException(ProductNotFoundException ex) {
        ProductErrorCode errorCode = ex.getErrorCode();
        UserResponseDTO responseDto = new UserResponseDTO(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }

    // 그 외 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserResponseDTO> handleGenericException(Exception ex) {
        GlobalErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        UserResponseDTO responseDto = new UserResponseDTO(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }
}
