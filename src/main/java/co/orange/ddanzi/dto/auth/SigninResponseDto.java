package co.orange.ddanzi.dto.auth;

import co.orange.ddanzi.domain.user.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SigninResponseDto {
    private String accesstoken;
    private String refreshtoken;
    private String nickname;
    private UserStatus status;
}
