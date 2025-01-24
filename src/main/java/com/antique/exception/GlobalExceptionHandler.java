package com.antique.exception;


import com.antique.dto.GenericResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<GenericResponseDTO> handleBaseException(BaseException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        GenericResponseDTO responseDto = new GenericResponseDTO(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }


    // 그 외 일반 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponseDTO> handleGenericException(Exception ex) {
        GenericResponseDTO responseDto = new GenericResponseDTO(
                null,
                "서버 에러가 발생했습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }
}
