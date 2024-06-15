package co.orange.ddanzi.domain.member;


import co.orange.ddanzi.domain.member.enums.AddressType;
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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "zip_code", nullable = false, length = 5)
    private String zipCode;         //우편번호

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AddressType type;       //주소종류(ROAD/LOT)

    @Column(name = "address", nullable = false)
    private String address;         //주소지

    @Column(name = "detail_address")
    private String detailAddress;   //상세 주소

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          //회원:주소=1:N

}
