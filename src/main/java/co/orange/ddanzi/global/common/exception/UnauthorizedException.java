package co.orange.ddanzi.global.common.exception;

import co.orange.ddanzi.global.common.error.Error;
import lombok.Getter;

@Getter
public class UnauthorizedException extends ApiException{

    public UnauthorizedException() {
        super(Error.UNAUTHORIZED_USER, Error.UNAUTHORIZED_USER.getMessage());
    }
    public UnauthorizedException(String message) {
        super(Error.UNAUTHORIZED_USER, message);
    }
    public UnauthorizedException(Error error) {
        super(error, error.getMessage());
    }
}
