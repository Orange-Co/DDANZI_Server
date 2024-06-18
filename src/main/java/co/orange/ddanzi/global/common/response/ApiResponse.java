package co.orange.ddanzi.global.common.response;

import co.orange.ddanzi.global.common.exception.Error;
import co.orange.ddanzi.global.config.handler.GlobalControllerHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Builder
public class ApiResponse<T> {

    @JsonProperty
    private int status;
    @JsonProperty
    private String message;
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime timestamp;
    @JsonProperty
    private String path;
    @JsonProperty
    private final T data;


    public static <T> ApiResponse<T> onSuccess(Success success, T data) {
        HttpServletRequest request = GlobalControllerHandler.getRequest();
        return new ApiResponse<>(
                success.getHttpStatusCode(),
                success.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI(),
                data
        );
    }

    public static <T> ApiResponse<T> onFailure(Error error, T data) {
        HttpServletRequest request = GlobalControllerHandler.getRequest();
        return new ApiResponse<>(
                error.getHttpStatusCode(),
                error.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI(),
                data
        );
    }
}
