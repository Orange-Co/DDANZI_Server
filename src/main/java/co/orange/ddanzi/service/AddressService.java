package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.user.Address;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.AddressInfo;
import co.orange.ddanzi.dto.setting.AddressResponseDto;
import co.orange.ddanzi.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressInfo setAddressInfo(User user){
        Address address = addressRepository.findByUser(user);
        return AddressInfo.builder()
                .recipient(address != null ? address.getRecipient() : null)
                .zipCode(address != null ? address.getZipCode() : null)
                .address(address != null ? address.getAddress() + " " + address.getDetailAddress() : null)
                .recipientPhone(address != null ? address.getRecipientPhone() : null)
                .build();
    }

    public AddressResponseDto setAddressDto(User user){
        Address address = addressRepository.findByUser(user);
        return AddressResponseDto.builder()
                .addressId(address != null ? address.getId() : null)
                .recipient(address != null ? address.getRecipient() : null)
                .zipCode(address != null ? address.getZipCode() : null)
                .type(address != null ? address.getType() : null)
                .address(address != null ? address.getAddress() : null)
                .detailAddress(address != null ? address.getDetailAddress() : null)
                .recipientPhone(address != null ? address.getRecipientPhone() : null)
                .build();
    }
}
