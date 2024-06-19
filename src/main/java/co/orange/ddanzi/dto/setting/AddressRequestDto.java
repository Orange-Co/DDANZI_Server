package co.orange.ddanzi.dto.setting;

import co.orange.ddanzi.domain.user.Address;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.enums.AddressType;
import lombok.Getter;

@Getter
public class AddressRequestDto {
    private String zipCode;
    private AddressType type;
    private String address;
    private String detailAddress;

    public Address toEntity(User user){
        return Address.builder()
                .user(user)
                .zipCode(zipCode)
                .type(type)
                .address(address)
                .detailAddress(detailAddress).build();
    }
}
