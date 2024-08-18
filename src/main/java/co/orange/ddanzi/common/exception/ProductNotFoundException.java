package co.orange.ddanzi.common.exception;

import co.orange.ddanzi.common.error.Error;

public class ProductNotFoundException extends ApiException{
    public ProductNotFoundException() {
        super(Error.PRODUCT_NOT_FOUND, Error.PRODUCT_NOT_FOUND.getMessage());
    }
}