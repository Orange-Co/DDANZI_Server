package co.orange.ddanzi.domain.member.enums;

import lombok.Getter;

@Getter
public enum Sex {
    MAN("남"),
    WOMAN("여")
    ;
    private final String sex;
    Sex(String sex) {
        this.sex = sex;
    }
}
