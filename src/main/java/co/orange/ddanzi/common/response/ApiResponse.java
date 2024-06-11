package co.orange.ddanzi.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Builder
public class ApiResponse<T> {
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            locale = "Asia/Seoul"
    )

    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private final T data;

    public static <T> ApiResponse<T> onSuccess(Success success, HttpServletRequest request, T data) {
        return new ApiResponse<>(
                success.getHttpStatusCode(),
                success.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI(),
                data
        );
    }

    public static <T> ApiResponse<T> onFailure(Error error, HttpServletRequest request, T data) {
        return new ApiResponse<>(
                error.getHttpStatusCode(),
                error.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI(),
                data
        );
    }
}
