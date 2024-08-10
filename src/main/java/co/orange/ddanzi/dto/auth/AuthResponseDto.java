package co.orange.ddanzi.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthResponseDto {
    private String accesstoken;
    private String nickname;
}
