package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
@RestController
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping
    ApiResponse<?> getAlarms(){
        return alarmService.getAlarms();
    }

    @PatchMapping("/{id}")
    ApiResponse<?> checkedAlarm(@PathVariable("id") Long id){
        return alarmService.checkedAlarm(id);
    }

}
