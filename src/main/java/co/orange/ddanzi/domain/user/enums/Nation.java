package co.orange.ddanzi.domain.user.enums;

import lombok.Getter;

@Getter
public enum Nation {
    KOR("대한민국"),
    JPN("일본"),
    USA("미국")
    ;

    private final String nation;
    Nation(String nation) {
        this.nation = nation;
    }
}
