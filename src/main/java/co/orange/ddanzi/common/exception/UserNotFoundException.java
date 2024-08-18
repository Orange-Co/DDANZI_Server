package co.orange.ddanzi.common.exception;

import co.orange.ddanzi.common.error.Error;

public class UserNotFoundException extends ApiException{
    public UserNotFoundException() {
        super(Error.USER_NOT_FOUND, Error.USER_NOT_FOUND.getMessage());
    }
}