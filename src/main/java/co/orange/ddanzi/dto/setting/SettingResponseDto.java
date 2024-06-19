package co.orange.ddanzi.dto.setting;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettingResponseDto {
    private String name;
    private String nickname;
    private String phone;
}
