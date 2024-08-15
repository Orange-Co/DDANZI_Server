package co.orange.ddanzi.global.config.handler;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.error.ErrorResponse;
import co.orange.ddanzi.common.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Order(1)
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // 비즈니스 로직 에러 처리
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final ApiException apiException, HttpServletRequest httpServletRequest) {
        log.error("handleBusinessException", apiException);
        final ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        return new ResponseEntity<>(ErrorResponse.onFailure(apiException.getError(), apiException.getMessage()), null, apiException.getError().getHttpStatusCode());
    }

    // 따로 처리하지 않은 500 에러 모두 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest httpServletRequest) {
        log.error("handleException", exception);
        final ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        return new ResponseEntity<>(ErrorResponse.onFailure(Error.INTERNAL_SERVER_ERROR), null, INTERNAL_SERVER_ERROR);
    }
}