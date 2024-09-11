package co.orange.ddanzi.dto.setting;

import co.orange.ddanzi.domain.user.Account;
import co.orange.ddanzi.domain.user.Bank;
import co.orange.ddanzi.domain.user.User;
import lombok.Getter;

@Getter
public class AccountRequestDto {
    private String accountName;
    private String bank;
    private String accountNumber;

    public Account toEntity(User user, Bank bank) {
        return Account.builder()
                .user(user)
                .bank(bank)
                .number(accountNumber)
                .build();

    }
}
