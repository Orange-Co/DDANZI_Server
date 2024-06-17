package co.orange.ddanzi.domain.product.enums;

import lombok.Getter;

@Getter
public enum OptionType {
    COLOR("색상"),
    SIZE("사이즈"),
    IMPRINT("각인"),
    ETC("기타")
    ;

    private final String optionType;
    OptionType(String optionType) {
        this.optionType = optionType;
    }
}
