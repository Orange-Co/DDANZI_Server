package co.orange.ddanzi.dto.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PortOneTokenResponseDto {
    private int code;
    private String message;
    private Response response;

    @Getter
    @Builder
    public static class Response {
        private String access_token;
        private long now;
        private long expired_at;

    }
}
