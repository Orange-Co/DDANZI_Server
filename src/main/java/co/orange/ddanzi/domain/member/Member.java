package co.orange.ddanzi.domain.member;

import co.orange.ddanzi.common.domain.BaseTimeEntity;
import co.orange.ddanzi.domain.member.enums.LoginType;
import co.orange.ddanzi.domain.member.enums.MemberStatus;
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
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LoginType type;     //로그인 타입(KAKAO/APPLE)

    @Column(name = "nickname")
    private String nickname;    // 닉네임 -> 자동생성

    @Column(name = "is_authenticated")
    private Boolean isAuthenticated = Boolean.FALSE;    //본인 인증 여부

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MemberStatus status;    //상태(ACTIVATE/SLEEP/DELETE)

}
