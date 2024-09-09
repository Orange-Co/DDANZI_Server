package co.orange.ddanzi.dto.alarm;

import co.orange.ddanzi.domain.user.enums.FcmCase;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyAlarm {
    private Long alarmId;
    private FcmCase alarmCase;
    private String title;
    private String content;
    private String time;
    private Boolean isChecked;
    private String orderId;
    private String itemId;
}
