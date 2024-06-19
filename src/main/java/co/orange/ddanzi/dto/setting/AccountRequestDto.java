package co.orange.ddanzi.dto.setting;

import co.orange.ddanzi.domain.user.Account;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.enums.Bank;
import lombok.Getter;

@Getter
public class AccountRequestDto {
    private String accountName;
    private Bank bank;
    private String accountNumber;

    public Account toEntity(User user){
        return Account.builder()
                .user(user)
                .bank(bank)
                .number(accountNumber)
                .build();

    }
}
