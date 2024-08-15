package co.orange.ddanzi.global.common.exception;

import co.orange.ddanzi.global.common.error.Error;

public class UserNotFoundException extends ApiException{
    public UserNotFoundException() {
        super(Error.USER_NOT_FOUND, Error.USER_NOT_FOUND.getMessage());
    }
}