package com.antique.exception.dibs;

import com.antique.exception.user.UserErrorCode;
import lombok.Getter;

@Getter
public class DibsNotFoundException extends RuntimeException {
    private final DibsErrorCode errorCode;

    public DibsNotFoundException() {
        super(DibsErrorCode.DIBS_NOT_FOUND.getMessage());
        this.errorCode = DibsErrorCode.DIBS_NOT_FOUND;
    }
}
