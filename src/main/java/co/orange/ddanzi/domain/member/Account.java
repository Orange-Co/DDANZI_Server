package co.orange.ddanzi.domain.member;

import co.orange.ddanzi.domain.member.enums.Bank;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;                //계좌 고유 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "bank")
    private Bank bank;              //은행 정보

    @Column(name = "number", unique = true, nullable = false)
    private String number;          //계좌 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
