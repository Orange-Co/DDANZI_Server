package co.orange.ddanzi.domain;

import co.orange.ddanzi.common.domain.BaseTimeEntity;
import co.orange.ddanzi.domain.enums.LoginType;
import jakarta.persistence.*;

@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column
    private LoginType type;

    @Column
    private String nickname;

    @Column(name = "is_authenticated")
    private Boolean isAuthenticated;

}
