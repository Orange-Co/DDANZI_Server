package co.orange.ddanzi.domain.user;

import co.orange.ddanzi.common.domain.BaseTimeEntity;
import co.orange.ddanzi.domain.user.enums.LoginType;
import co.orange.ddanzi.domain.user.enums.UserStatus;
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

    @Column(name = "email", nullable = false, length = 320)
    private String email;     //가입 이메일

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LoginType type;     //로그인 타입(KAKAO/APPLE)

    @Column(name = "nickname", nullable = false, unique = true, length = 20)
    private String nickname;    // 닉네임 -> 자동생성

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;    //상태(ACTIVATE/SLEEP/DELETE)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authentication_id")
    private Authentication authentication;

    @Builder
    public User(String email, LoginType type, String nickname, UserStatus status) {
        this.email = email;
        this.nickname = nickname;
        this.type = type;
        this.status = status;
    }

    public void setAuthentication(UserStatus status, Authentication authentication) {
        this.status = status;
        this.authentication = authentication;
    }
}
