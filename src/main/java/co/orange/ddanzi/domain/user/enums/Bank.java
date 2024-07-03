package co.orange.ddanzi.domain.user.enums;

import lombok.Getter;

@Getter
public enum Bank {
    GUKMIN("국민은행"),
    SINHAN("신한은행"),
    HANA("하나은행")
    ;
    private final String bank;
    Bank(final String bank) {
        this.bank = bank;
    }
}
