package com.antique.exception;


import com.antique.dto.GenericResponseDTO;
import com.antique.exception.dibs.DibsAlreadyExistException;
import com.antique.exception.dibs.DibsErrorCode;
import com.antique.exception.dibs.DibsNotFoundException;
import com.antique.exception.product.ProductErrorCode;
import com.antique.exception.product.ProductNotFoundException;
import com.antique.dto.user.UserResponseDTO;
import com.antique.exception.review.ReviewErrorCode;
import com.antique.exception.review.ReviewNotFoundException;
import com.antique.exception.user.UserErrorCode;
import com.antique.exception.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DibsAlreadyExistException.class)
    public ResponseEntity<GenericResponseDTO> handleDibsAlreadyExistException(DibsAlreadyExistException ex) {
        DibsErrorCode errorCode = ex.getErrorCode();
        GenericResponseDTO responseDto = new GenericResponseDTO(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }

    // UserNotFoundException 처리
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GenericResponseDTO> handleUserNotFoundException(UserNotFoundException ex) {
        UserErrorCode errorCode = ex.getErrorCode();
        GenericResponseDTO responseDto = new GenericResponseDTO(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }

    // ProductNotFoundException 처리
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<GenericResponseDTO> handleProductNotFoundException(ProductNotFoundException ex) {
        ProductErrorCode errorCode = ex.getErrorCode();
        GenericResponseDTO responseDto = new GenericResponseDTO(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }

    // ProductNotFoundException 처리
    @ExceptionHandler(DibsNotFoundException.class)
    public ResponseEntity<GenericResponseDTO> handleDibsNotFoundException(DibsNotFoundException ex) {
        DibsErrorCode errorCode = ex.getErrorCode();
        GenericResponseDTO responseDto = new GenericResponseDTO(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }
  
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<UserResponseDTO> handleReviewNotFoundException(ReviewNotFoundException ex) {
        ReviewErrorCode errorCode = ex.getErrorCode();
        UserResponseDTO responseDto = new UserResponseDTO(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }

    // 그 외 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponseDTO> handleGenericException(Exception ex) {
        GlobalErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        GenericResponseDTO responseDto = new GenericResponseDTO(
                null,
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(responseDto);
    }
}
