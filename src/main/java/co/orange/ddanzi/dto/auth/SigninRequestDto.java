package co.orange.ddanzi.dto.auth;

import co.orange.ddanzi.domain.user.enums.LoginType;
import lombok.Getter;

@Getter
public class SigninRequestDto {
    private String token;
    private LoginType type;
    private String devicetoken;
    private Boolean isAgreedMarketingTerm;
}
