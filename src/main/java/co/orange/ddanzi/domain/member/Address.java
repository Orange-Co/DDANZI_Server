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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "zip_code", nullable = false, length = 5)
    private String zipCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AddressType type;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

}
