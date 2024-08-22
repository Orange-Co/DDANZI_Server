package co.orange.ddanzi.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenResponseDto {
    private String accesstoken;
    private String refreshtoken;
}
