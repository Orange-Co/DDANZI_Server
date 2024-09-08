package co.orange.ddanzi.dto.alarm;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AlarmResponseDto {
    private List<MyAlarm> alarmList;
}
