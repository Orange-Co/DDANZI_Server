package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.user.Address;
import co.orange.ddanzi.domain.user.Authentication;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.setting.AddressRequestDto;
import co.orange.ddanzi.dto.setting.AddressResponseDto;
import co.orange.ddanzi.dto.setting.SettingResponseDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.AddressRepository;
import co.orange.ddanzi.repository.AuthenticationRepository;
import co.orange.ddanzi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SettingService {
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public ApiResponse<?> getSetting(){
        User user = userRepository.findById(1L).orElse(null);
        Authentication authentication = authenticationRepository.findById(user.getId()).orElse(null);

        String name = null; String phone = null;
        if(authentication != null){
            name = authentication.getName();
            phone = authentication.getPhone();
        }

        SettingResponseDto responseDto = SettingResponseDto.builder()
                .name(name)
                .nickname(user.getNickname())
                .phone(phone)
                .build();

        return ApiResponse.onSuccess(Success.GET_SETTING_SCREEN_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> getAddress(){
        User user = userRepository.findById(1L).orElse(null);
        Address address = addressRepository.findById(user.getId()).orElse(null);
        AddressResponseDto responseDto = setAddressDto(address, user.getAuthentication());
        return ApiResponse.onSuccess(Success.GET_SETTING_ADDRESS_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> addAddress(AddressRequestDto requestDto){
        User user = userRepository.findById(1L).orElse(null);
        Address newAddress = requestDto.toEntity(user);
        newAddress = addressRepository.save(newAddress);
        AddressResponseDto responseDto = setAddressDto(newAddress, user.getAuthentication());
        return ApiResponse.onSuccess(Success.CREATE_ADDRESS_SUCCESS, responseDto);
    }

    private AddressResponseDto setAddressDto(Address address, Authentication authentication){
        return AddressResponseDto.builder()
                .addressId(address != null ? address.getId() : null)
                .name(authentication != null ? authentication.getName() : null)
                .zipCode(address != null ? address.getZipCode() : null)
                .type(address != null ? address.getType() : null)
                .address(address != null ? address.getAddress() : null)
                .detailAddress(address != null ? address.getDetailAddress() : null)
                .phone(authentication != null ? authentication.getPhone() : null)
                .build();
    }
}
