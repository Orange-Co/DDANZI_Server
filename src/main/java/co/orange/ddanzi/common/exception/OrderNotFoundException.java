package co.orange.ddanzi.common.exception;

import co.orange.ddanzi.common.error.Error;

public class OrderNotFoundException extends ApiException {
    public OrderNotFoundException() {
        super(Error.ORDER_NOT_FOUND, Error.ORDER_NOT_FOUND.getMessage());
    }
}
