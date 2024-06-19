package co.orange.ddanzi.dto.setting;

import co.orange.ddanzi.domain.user.enums.Bank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountResponseDto {
    private Long accountId;
    private String name;
    private Bank bank;
    private String accountNumber;
}
