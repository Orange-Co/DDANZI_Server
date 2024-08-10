package co.orange.ddanzi.dto.setting;

import co.orange.ddanzi.domain.user.enums.AddressType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponseDto {
    private Long addressId;
    private String recipient;
    private String zipCode;
    private AddressType type;
    private String address;
    private String detailAddress;
    private String recipientPhone;
}
