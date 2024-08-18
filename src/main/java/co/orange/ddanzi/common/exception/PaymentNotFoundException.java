package co.orange.ddanzi.common.exception;

import co.orange.ddanzi.common.error.Error;

public class PaymentNotFoundException extends ApiException {
    public PaymentNotFoundException() {
        super(Error.PAYMENT_NOT_FOUND, Error.PAYMENT_NOT_FOUND.getMessage());
    }
}
