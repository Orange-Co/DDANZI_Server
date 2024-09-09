package co.orange.ddanzi.domain.user;

import co.orange.ddanzi.common.domain.BaseTimeEntity;
import co.orange.ddanzi.domain.user.enums.Nation;
import co.orange.ddanzi.domain.user.enums.Sex;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Table(name = "authentications")
@Entity
public class Authentication extends BaseTimeEntity {
    @Id
    @Column(name = "user_id")
    private Long id;      //멤버 고유 ID (PK/FK)

    @MapsId
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="ci")
    private String ci;

    @Column(name = "name", nullable = false, length = 255)
    private String name;                    //이름

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;                   //전화번호

    @Column(name = "birth", nullable = false)
    private LocalDate birth;                //생년월일

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex;                        //성별

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'KOR'")
    @Column(name = "nation", nullable = false)
    private Nation nation;                  //국가,디폴트 한국


    @Builder
    public Authentication(User user, String ci, String name, String phone, LocalDate birth, Sex sex, Nation nation) {
        this.user = user;
        this.ci = ci;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
        this.sex = sex;
        this.nation = nation;
    }
}
