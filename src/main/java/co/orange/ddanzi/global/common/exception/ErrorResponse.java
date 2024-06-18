package co.orange.ddanzi.global.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
public class ErrorResponse {

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            locale = "Asia/Seoul"
    )

    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private List<VaildationError> vaildErrors;

    public static ErrorResponse onFailure(Error error, HttpServletRequest request) {
        return ErrorResponse.builder()
                .status(error.getHttpStatus().value())
                .message(error.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
    }

    public static ErrorResponse onFailure(Error error, String message) {
        return ErrorResponse.builder()
                .status(error.getHttpStatus().value())
                .message(message)
                .build();
    }

    @Data
    @RequiredArgsConstructor
    private static class VaildationError {
        private final  String field;
        private final String message;
    }

    public void addVaildationError(String field, String message) {
        if (Objects.isNull(vaildErrors)){
            vaildErrors = new ArrayList<>();
        }
        vaildErrors.add(new VaildationError(field, message));
    }


}
