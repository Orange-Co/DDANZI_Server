package co.orange.ddanzi.dto.setting;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountResponseDto {
    private Long accountId;
    private String name;
    private String bank;
    private String accountNumber;
}
