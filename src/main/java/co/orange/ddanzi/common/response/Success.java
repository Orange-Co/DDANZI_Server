package co.orange.ddanzi.common.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Success {
    // Default
    SUCCESS(HttpStatus.OK, "Request successfully processed"),

    // 200 OK SUCCESS

    // 201 CREATED

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
