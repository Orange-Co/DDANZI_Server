package co.orange.ddanzi.domain.user.enums;

import lombok.Getter;

@Getter
public enum MemberStatus {
    ACTIVATE("활성"),
    SLEEP("휴면"),
    DELETE("삭제")
    ;

    private final String memberStatus;
    MemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

}
