package co.orange.ddanzi.global.common.exception;

import co.orange.ddanzi.global.common.error.Error;

public class ProductNotFoundException extends ApiException{
    public ProductNotFoundException() {
        super(Error.PRODUCT_NOT_FOUND, Error.PRODUCT_NOT_FOUND.getMessage());
    }
}