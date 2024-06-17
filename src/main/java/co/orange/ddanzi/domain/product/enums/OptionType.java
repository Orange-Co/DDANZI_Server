package co.orange.ddanzi.domain.product.enums;

import lombok.Getter;

@Getter
public enum OptionType {
    COLOR("COLOR"),
    SIZE("SIZE"),
    IMPRINT("IMPRINT"),
    ETC("ETC")
    ;

    private final String optionType;
    OptionType(String optionType) {
        this.optionType = optionType;
    }
}
