package co.orange.ddanzi.common.exception;

import co.orange.ddanzi.common.error.Error;

public class DiscountNotFoundException extends ApiException{
    public DiscountNotFoundException() {
        super(Error.DISCOUNT_INFO_NOT_FOUND, Error.DISCOUNT_INFO_NOT_FOUND.getMessage());
    }
}