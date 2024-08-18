package co.orange.ddanzi.common.exception;

import co.orange.ddanzi.common.error.Error;

public class AddressNotFoundException extends ApiException{
    public AddressNotFoundException() {
        super(Error.ADDRESS_NOT_FOUND, Error.ADDRESS_NOT_FOUND.getMessage());
    }
}
