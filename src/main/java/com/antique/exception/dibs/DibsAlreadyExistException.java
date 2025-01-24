package com.antique.exception.dibs;

import lombok.Getter;

@Getter
public class DibsAlreadyExistException extends RuntimeException {
    private final DibsErrorCode errorCode;

    public DibsAlreadyExistException() {
        super(DibsErrorCode.DIBS_ALREADY_EXISTS.getMessage());
        this.errorCode = DibsErrorCode.DIBS_ALREADY_EXISTS;
    }
}
