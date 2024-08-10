package co.orange.ddanzi.domain.user.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    UNAUTHENTICATED("인증안됨"),
    ACTIVATE("활성"),
    SLEEP("휴면"),
    DELETE("삭제")
    ;

    private final String userStatus;
    UserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

}
