package com.antique.exception.product;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private final ProductErrorCode errorCode;

    public ProductNotFoundException(ProductErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ProductErrorCode getErrorCode() {
        return errorCode;
    }
}