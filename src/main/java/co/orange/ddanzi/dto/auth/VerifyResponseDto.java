package co.orange.ddanzi.dto.auth;

import co.orange.ddanzi.domain.user.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerifyResponseDto {
    private String nickname;
    private String phone;
    private UserStatus status;
}
