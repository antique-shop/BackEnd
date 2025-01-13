package com.antique.exception.product;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private final ProductErrorCode errorCode;

    public ProductNotFoundException(ProductErrorCode errorCode) {
        this.errorCode = ProductErrorCode.PRODUCT_NOT_FOUND;
    }

    public ProductErrorCode getErrorCode() {
        return errorCode;
    }
}