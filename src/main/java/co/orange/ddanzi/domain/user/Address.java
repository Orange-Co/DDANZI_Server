package co.orange.ddanzi.domain.user;


import co.orange.ddanzi.domain.user.enums.AddressType;
import co.orange.ddanzi.dto.setting.AddressRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "addresses")
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;                //주소 고유 ID

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "recipient_phone")
    private String recipientPhone;

    @Column(name = "zip_code", nullable = false, length = 5)
    private String zipCode;         //우편번호

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AddressType type;       //주소종류(ROAD/LOT)

    @Column(name = "address", nullable = false)
    private String address;         //주소지

    @Column(name = "detail_address")
    private String detailAddress;   //상세 주소

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;          //회원:주소=1:N

    @Builder
    public Address(String zipCode, String recipient, String recipientPhone, AddressType type, String address, String detailAddress, User user) {
        this.zipCode = zipCode;
        this.recipient = recipient;
        this.recipientPhone = recipientPhone;
        this.type = type;
        this.address = address;
        this.detailAddress = detailAddress;
        this.user = user;
    }

    public void update(AddressRequestDto requestDto){
        this.zipCode = requestDto.getZipCode();
        this.recipient = requestDto.getRecipient();
        this.recipientPhone = requestDto.getRecipientPhone();
        this.type = requestDto.getType();
        this.address = requestDto.getAddress();
        this.detailAddress = requestDto.getDetailAddress();
    }
}
