package co.orange.ddanzi.controller;

import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mypage/setting")
public class SettingController {
    private final SettingService settingService;

    @GetMapping
    ApiResponse<?> getSetting(){
        return settingService.getSetting();
    }
}
