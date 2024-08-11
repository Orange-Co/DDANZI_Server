package co.orange.ddanzi.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerifyResponseDto {
    private String nickname;
    private String phone;
}
