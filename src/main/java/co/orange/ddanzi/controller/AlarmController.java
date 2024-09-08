package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
@RestController
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping
    ApiResponse<?> getAlarms(){
        return alarmService.getAlarms();
    }
}
