package co.orange.ddanzi.controller;

import co.orange.ddanzi.dto.setting.AddressRequestDto;
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

    @GetMapping("/address")
    ApiResponse<?> getAddress(){
        return  settingService.getAddress();
    }

    @PostMapping("/address")
    ApiResponse<?> addAddress(@RequestBody AddressRequestDto requestDto){
        return  settingService.addAddress(requestDto);
    }

}
