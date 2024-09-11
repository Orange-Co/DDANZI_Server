package co.orange.ddanzi.domain.user;


import co.orange.ddanzi.dto.setting.AccountRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "accounts")
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;                //계좌 고유 ID

    @Column(name = "number", unique = true, nullable = false)
    private String number;          //계좌 번호

    @OneToOne(fetch = FetchType.LAZY)
    private Bank bank;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;          //계좌 보유 유저

    @Builder
    public Account(Bank bank, String number, User user) {
        this.bank = bank;
        this.number = number;
        this.user = user;
    }

    public void updateAccount(Bank bank, String number) {
        this.bank = bank;
        this.number = number;
    }
}
