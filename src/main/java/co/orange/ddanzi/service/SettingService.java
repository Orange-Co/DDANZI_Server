package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.user.*;
import co.orange.ddanzi.dto.setting.*;
import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.AccountRepository;
import co.orange.ddanzi.repository.AddressRepository;
import co.orange.ddanzi.repository.BankRepository;
import co.orange.ddanzi.repository.PushAlarmRepository;
import co.orange.ddanzi.service.common.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SettingService {
    private final AuthUtils authUtils;
    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;
    private final PushAlarmRepository pushAlarmRepository;

    @Autowired
    AddressService addressService;

    @Transactional
    public ApiResponse<?> getSetting(){
        User user = authUtils.getUser();
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
    public ApiResponse<?> enterAddress(){
        User user = authUtils.getUser();
        return ApiResponse.onSuccess(Success.GET_SETTING_ADDRESS_SUCCESS, EnterAddressResponseDto.builder()
                .name(user.getAuthentication().getName())
                .phone(user.getAuthentication().getPhone())
                .build());
    }

    @Transactional
    public ApiResponse<?> getAddress(){
        User user = authUtils.getUser();
        AddressResponseDto responseDto = addressService.setAddressDto(user);
        return ApiResponse.onSuccess(Success.GET_SETTING_ADDRESS_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> addAddress(AddressRequestDto requestDto){
        User user = authUtils.getUser();
        Address newAddress = requestDto.toEntity(user);
        addressRepository.save(newAddress);
        AddressResponseDto responseDto = addressService.setAddressDto(user);
        return ApiResponse.onSuccess(Success.CREATE_ADDRESS_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> updateAddress(Long addressId, AddressRequestDto requestDto){
        User user = authUtils.getUser();
        Address updatedAddress = addressRepository.findById(addressId).orElse(null);
        if(updatedAddress == null){
            return ApiResponse.onFailure(Error.ADDRESS_NOT_FOUND, null);
        }
        updatedAddress.update(requestDto);
        AddressResponseDto responseDto = addressService.setAddressDto(user);
        return ApiResponse.onSuccess(Success.PUT_ADDRESS_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> deleteAddress(Long addressId){
        Address deletedAddress = addressRepository.findById(addressId).orElse(null);
        if(deletedAddress == null){
            return ApiResponse.onFailure(Error.ADDRESS_NOT_FOUND, null);
        }
        addressRepository.delete(deletedAddress);
        return ApiResponse.onSuccess(Success.DELETE_ADDRESS_SUCCESS, true);
    }

    @Transactional
    public ApiResponse<?> getAccount(){
        User user = authUtils.getUser();
        Account account = accountRepository.findByUserId(user);
        AccountResponseDto responseDto = setAccountDto(account, user.getAuthentication());
        return ApiResponse.onSuccess(Success.GET_SETTING_ACCOUNT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> addAccount(AccountRequestDto requestDto){
        User user = authUtils.getUser();
        Authentication authentication = user.getAuthentication();

        log.info("본인인증 여부 확인");
        if(authentication == null)
            return ApiResponse.onFailure(Error.AUTHENTICATION_INFO_NOT_FOUND, null);

        log.info("회원 이름과 예금주가 동일한지 검증");
        if(!authentication.getName().equals(requestDto.getAccountName()))
            return ApiResponse.onFailure(Error.ACCOUNT_NAME_DOES_NOT_MATCH, null);

        log.info("계좌 중복 여부 확인");
        boolean accountExists = accountRepository.existsByNumber(requestDto.getAccountNumber());
        if (accountExists)
            return ApiResponse.onFailure(Error.ACCOUNT_ALREADY_EXISTS, null);

        Bank bank = bankRepository.findByBankCode(requestDto.getBank());
        log.info("계좌 생성");
        Account newAccount = requestDto.toEntity(user, bank);
        newAccount = accountRepository.save(newAccount);

        AccountResponseDto responseDto = setAccountDto(newAccount, user.getAuthentication());
        return ApiResponse.onSuccess(Success.CREATE_ACCOUNT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> updateAccount(Long accountId, AccountRequestDto requestDto){
        User user = authUtils.getUser();
        Account updatedAccount = accountRepository.findById(accountId).orElse(null);
        if(updatedAccount == null){
            return ApiResponse.onFailure(Error.ACCOUNT_NOT_FOUND, null);
        }
        Bank bank = bankRepository.findByBankCode(requestDto.getBank());
        updatedAccount.updateAccount(bank, requestDto.getAccountNumber());
        AccountResponseDto responseDto = setAccountDto(updatedAccount, user.getAuthentication());
        return ApiResponse.onSuccess(Success.PUT_ACCOUNT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> deleteAccount(Long accountId){
        Account deeletedAccount= accountRepository.findById(accountId).orElse(null);
        if(deeletedAccount == null){
            return ApiResponse.onFailure(Error.ACCOUNT_NOT_FOUND, null);
        }
        accountRepository.delete(deeletedAccount);
        return ApiResponse.onSuccess(Success.DELETE_ACCOUNT_SUCCESS, null);
    }

    @Transactional
    public ApiResponse<?> updatePushAlarm(PushAlarmRequestDto requestDto){
        User user = authUtils.getUser();
        PushAlarm pushAlarm = pushAlarmRepository.findByUser(user).orElse(null);
        if(pushAlarm == null){
            return ApiResponse.onFailure(Error.PUSH_ALARM_NOT_FOUND, null);
        }
        pushAlarm.update(requestDto);
        return ApiResponse.onSuccess(Success.PUT_PUSH_ALARM_SUCCESS, null);
    }

    private AccountResponseDto setAccountDto(Account account, Authentication authentication){
        return AccountResponseDto.builder()
                .accountId(account != null ? account.getId() : null)
                .name(authentication != null ? authentication.getName() : null)
                .bank(account != null ? account.getBank().getBankCode() : null)
                .accountNumber(account != null ? account.getNumber() : null)
                .build();
    }
}
