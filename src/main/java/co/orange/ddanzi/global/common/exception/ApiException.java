package co.orange.ddanzi.global.common.exception;

import co.orange.ddanzi.global.common.error.Error;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final Error error;

    public ApiException(Error error){
        super(error.getMessage());
        this.error = error;
    }

    public int getHttpStatus(){
        return error.getHttpStatusCode();
    }
}