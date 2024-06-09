package co.orange.ddanzi.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;
    private String message;
    private LocalDateTime time;
    private String path;
    private String trace;
    private List<VaildationError> vaildErrors;

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

    public ErrorResponse(int status, String message, String path, LocalDateTime time) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.time = time;
    }
}
