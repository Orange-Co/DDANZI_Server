package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.user.Account;
import co.orange.ddanzi.domain.user.Address;
import co.orange.ddanzi.domain.user.Authentication;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.setting.*;
import co.orange.ddanzi.global.common.exception.Error;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.AccountRepository;
import co.orange.ddanzi.repository.AddressRepository;
import co.orange.ddanzi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SettingService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public ApiResponse<?> getSetting(){
        User user = userRepository.findById(1L).orElse(null);
        Authentication authentication = user.getAuthentication();
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
        Address address = addressRepository.findByUser(user);
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

    @Transactional
    public ApiResponse<?> updateAddress(Long addressId, AddressRequestDto requestDto){
        User user = userRepository.findById(1L).orElse(null);
        Address updatedAddress = addressRepository.findById(addressId).orElse(null);
        if(updatedAddress == null){
            return ApiResponse.onFailure(Error.ADDRESS_NOT_FOUND, null);
        }
        updatedAddress.update(requestDto);
        AddressResponseDto responseDto = setAddressDto(updatedAddress, user.getAuthentication());
        return ApiResponse.onSuccess(Success.PUT_ADDRESS_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> deleteAddress(Long addressId){
        Address deletedAddress = addressRepository.findById(addressId).orElse(null);
        if(deletedAddress == null){
            return ApiResponse.onFailure(Error.ADDRESS_NOT_FOUND, null);
        }
        addressRepository.delete(deletedAddress);
        return ApiResponse.onSuccess(Success.DELETE_ADDRESS_SUCCESS, null);
    }

    @Transactional
    public ApiResponse<?> getAccount(){
        User user = userRepository.findById(1L).orElse(null);
        Account account = accountRepository.findByUserId(user);
        AccountResponseDto responseDto = setAccountDto(account, user.getAuthentication());
        return ApiResponse.onSuccess(Success.GET_SETTING_ACCOUNT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> addAccount(AccountRequestDto requestDto){
        User user = userRepository.findById(1L).orElse(null);
        Authentication authentication = user.getAuthentication();
        log.info("본인인증 여부 확인");
        if(authentication == null)
            return ApiResponse.onFailure(Error.AUTHENTICATION_INFO_NOT_FOUND, null);
        log.info("회원 이름과 예금주가 동일한지 검증");
        if(!authentication.getName().equals(requestDto.getAccountName()))
            return ApiResponse.onFailure(Error.ACCOUNT_NAME_DOES_NOT_MATCH, null);

        log.info("계좌 생성");
        Account newAccount = requestDto.toEntity(user);
        newAccount = accountRepository.save(newAccount);

        AccountResponseDto responseDto = setAccountDto(newAccount, user.getAuthentication());
        return ApiResponse.onSuccess(Success.CREATE_ACCOUNT_SUCCESS, null);
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

    private AccountResponseDto setAccountDto(Account account, Authentication authentication){
        return AccountResponseDto.builder()
                .name(authentication != null ? authentication.getName() : null)
                .bank(account != null ? account.getBank() : null)
                .accountNumber(account != null ? account.getNumber() : null)
                .build();
    }
}