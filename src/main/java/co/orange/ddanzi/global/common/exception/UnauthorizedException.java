package co.orange.ddanzi.global.common.exception;

import co.orange.ddanzi.global.common.error.Error;

public class UnauthorizedException extends ApiException{
    public UnauthorizedException(Error error) {
        super(error);
    }
}