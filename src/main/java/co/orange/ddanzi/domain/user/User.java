package co.orange.ddanzi.domain.user;

import co.orange.ddanzi.global.common.domain.BaseTimeEntity;
import co.orange.ddanzi.domain.user.enums.LoginType;
import co.orange.ddanzi.domain.user.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;            //유저 고유 ID

    @Column(name = "login_id")
    private String loginId;     //로그인 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LoginType type;     //로그인 타입(KAKAO/APPLE)

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;    // 닉네임 -> 자동생성

    @Column(name = "is_authenticated")
    private Boolean isAuthenticated = Boolean.FALSE;    //본인 인증 여부

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MemberStatus status;    //상태(ACTIVATE/SLEEP/DELETE)

    @Builder
    public User(String loginId, LoginType type, String nickname, MemberStatus status) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.type = type;
        this.status = status;
    }
}
