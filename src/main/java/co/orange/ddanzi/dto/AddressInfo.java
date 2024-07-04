package co.orange.ddanzi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressInfo {
    private String recipient;
    private String zipCode;
    private String address;
    private String phone;
}
