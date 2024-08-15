package co.orange.ddanzi.common.error;

import co.orange.ddanzi.global.config.handler.GlobalControllerHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ErrorResponse {

    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    private int status;
    private String message;
    private String path;
    private Object data;


    @Builder
    public ErrorResponse(int status, String message,String path, Object data) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.data = data;
    }

    public static ErrorResponse onFailure(Error error) {
        HttpServletRequest request = GlobalControllerHandler.getRequest();
        return ErrorResponse.builder()
                .status(error.getHttpStatus().value())
                .message(error.getMessage())
                .path(request.getRequestURI())
                .data(null)
                .build();
    }

    public static ErrorResponse onFailure(Error error, String message) {
        HttpServletRequest request = GlobalControllerHandler.getRequest();
        return ErrorResponse.builder()
                .status(error.getHttpStatus().value())
                .message(message)
                .path(request.getRequestURI())
                .data(null)
                .build();
    }

}