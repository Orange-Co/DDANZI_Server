package co.orange.ddanzi.controller;

import co.orange.ddanzi.dto.setting.AccountRequestDto;
import co.orange.ddanzi.dto.setting.AddressRequestDto;
import co.orange.ddanzi.dto.setting.PushAlarmRequestDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mypage/setting")
public class SettingController {
    private final SettingService settingService;

    @GetMapping
    ApiResponse<?> getSetting(){
        return settingService.getSetting();
    }

    @GetMapping("/address/enter")
    ApiResponse<?> enterAddress(){
        return  settingService.enterAddress();
    }

    @GetMapping("/address")
    ApiResponse<?> getAddress(){
        return  settingService.getAddress();
    }

    @PostMapping("/address")
    ApiResponse<?> addAddress(@RequestBody AddressRequestDto requestDto){
        return  settingService.addAddress(requestDto);
    }

    @PutMapping("/address/{id}")
    ApiResponse<?> updateAddress(@PathVariable Long id, @RequestBody AddressRequestDto requestDto){
        return  settingService.updateAddress(id, requestDto);
    }

    @DeleteMapping("/address/{id}")
    ApiResponse<?> deleteAddress(@PathVariable Long id){
        return  settingService.deleteAddress(id);
    }

    @GetMapping("/account")
    ApiResponse<?> getAccount(){
        return settingService.getAccount();
    }

    @PostMapping("/account")
    ApiResponse<?> addAccount(@RequestBody AccountRequestDto requestDto){
        return settingService.addAccount(requestDto);
    }

    @PutMapping("/account/{id}")
    ApiResponse<?> updateAccount(@PathVariable Long id, @RequestBody AccountRequestDto requestDto){
        return settingService.updateAccount(id, requestDto);
    }

    @DeleteMapping("/account/{id}")
    ApiResponse<?> deleteAccount(@PathVariable Long id){
        return settingService.deleteAccount(id);
    }

    @PutMapping("/pushAlarm")
    ApiResponse<?> updatePushAlarm(@RequestBody PushAlarmRequestDto requestDto){
        return settingService.updatePushAlarm(requestDto);
    }
}
