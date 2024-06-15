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
    private LoginType type;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "is_authenticated")
    private Boolean isAuthenticated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MemberStatus status;

}
