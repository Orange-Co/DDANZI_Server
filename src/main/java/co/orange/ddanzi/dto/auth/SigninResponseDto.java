package co.orange.ddanzi.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SigninResponseDto {
    private String accesstoken;
    private String refreshtoken;
    private String nickname;
}
