package co.orange.ddanzi.dto.auth;

import co.orange.ddanzi.domain.user.enums.LoginType;
import lombok.Getter;

@Getter
public class LoginDto {
    private String idToken;
    private LoginType type;

}
