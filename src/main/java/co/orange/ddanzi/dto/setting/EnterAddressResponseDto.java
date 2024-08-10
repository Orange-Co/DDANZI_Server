package co.orange.ddanzi.dto.setting;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnterAddressResponseDto {
    private String name;
    private String phone;
}
