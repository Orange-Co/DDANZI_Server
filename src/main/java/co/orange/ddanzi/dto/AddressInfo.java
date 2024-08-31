package co.orange.ddanzi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AddressInfo {
    private String recipient;
    private String zipCode;
    private String address;
    private String recipientPhone;
}
