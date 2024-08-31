package co.orange.ddanzi.dto.common;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AddressSeparateInfo extends AddressInfo{
    private String detailAddress;
}
