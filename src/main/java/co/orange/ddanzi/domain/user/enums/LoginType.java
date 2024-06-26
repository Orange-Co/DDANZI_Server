package co.orange.ddanzi.domain.user.enums;

import lombok.Getter;

@Getter
public enum LoginType {
    KAKAO("KAKAO"),
    APPLE("APPLE")
    ;

    private final String loginType;
    LoginType(String loginType) {
        this.loginType = loginType;
    }
}
