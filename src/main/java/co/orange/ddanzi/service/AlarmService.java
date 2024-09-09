package co.orange.ddanzi.service;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.user.Alarm;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.enums.FcmCase;
import co.orange.ddanzi.dto.alarm.MyAlarm;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class AlarmService {
    private final AuthUtils authUtils;
    private final AlarmRepository alarmRepository;

    public ApiResponse<?> getAlarms(){
        User user = authUtils.getUser();
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Alarm> alarmList = alarmRepository.findRecentAlarmsByUser(user, sevenDaysAgo);
        List<MyAlarm> myAlarmList = new ArrayList<>();
        for(Alarm alarm : alarmList){
            Order order = alarm.getOrder();
            FcmCase alarmCase = alarm.getAlarmCase();
            MyAlarm.MyAlarmBuilder myAlarm = MyAlarm.builder()
                    .alarmId(alarm.getId())
                    .alarmCase(alarmCase)
                    .title(alarmCase.getTitle())
                    .content(alarmCase.getBody())
                    .time(createAlarmTime(alarm.getCreatedAt()))
                    .isChecked(alarm.getIsChecked());

            if(alarmCase == FcmCase.A1 || alarmCase == FcmCase.A2 || alarmCase == FcmCase.A3 || alarmCase == FcmCase.A4){
                myAlarm.itemId(order.getItem().getId());
            }
            else
                myAlarm.orderId(order.getId());
        }
        return ApiResponse.onSuccess(Success.GET_ALARM_LIST_SUCCESS,myAlarmList);
    }

    private String createAlarmTime(LocalDateTime createdAt){
        Duration duration = Duration.between(createdAt, LocalDateTime.now());
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        if (hours < 1) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else {
            return createdAt.toLocalDate().toString();
        }
    }

}
