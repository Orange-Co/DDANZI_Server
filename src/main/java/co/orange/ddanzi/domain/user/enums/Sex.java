package co.orange.ddanzi.domain.user.enums;

import lombok.Getter;

@Getter
public enum Sex {
    MALE("MALE"),
    FEMALE("FEMALE")
    ;
    private final String sex;
    Sex(String sex) {
        this.sex = sex;
    }
}
