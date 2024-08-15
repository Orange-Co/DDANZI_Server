package co.orange.ddanzi.global.common.exception;

import co.orange.ddanzi.global.common.error.Error;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final Error error;
    private final String message;
}