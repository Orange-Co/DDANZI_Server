package co.orange.ddanzi.domain.member;

import co.orange.ddanzi.common.domain.BaseTimeEntity;
import co.orange.ddanzi.domain.member.enums.Nation;
import co.orange.ddanzi.domain.member.enums.Sex;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class Authentication extends BaseTimeEntity {
    @Id
    @Column(name = "member_id")
    private Long id;      //멤버 고유 ID (PK/FK)

    @MapsId
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "name", nullable = false, length = 255)
    private String name;                    //이름

    @Column(name = "email", nullable = false, length = 320)
    private String email;                   //이메일

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;                   //전화번호

    @Column(name = "birth", nullable = false)
    private LocalDate birth;                //생년월일

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex;                        //성별

    @Enumerated(EnumType.STRING)
    @Column(name = "nation", nullable = false)
    private Nation nation = Nation.KOR;     //국가,디폴트 한국


    @Builder
    public Authentication(Long id, Member member, String name, String email, String phone, LocalDate birth, Sex sex, Nation nation) {
        this.id = id;
        this.member = member;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birth = birth;
        this.sex = sex;
        this.nation = nation;
    }
}
