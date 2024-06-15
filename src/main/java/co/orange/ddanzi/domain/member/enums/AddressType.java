package co.orange.ddanzi.domain.member.enums;

import lombok.Getter;

@Getter
public enum AddressType {
    ROAD("도로명"),
    LOT("지번")
    ;

    private final String addressType;
    AddressType(String addressType) {
        this.addressType = addressType;
    }
}
