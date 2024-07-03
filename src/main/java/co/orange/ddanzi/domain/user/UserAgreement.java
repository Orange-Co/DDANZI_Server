package co.orange.ddanzi.domain.user;

import co.orange.ddanzi.global.common.domain.BaseTimeEntity;
import co.orange.ddanzi.domain.user.pk.UserAgreementId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Table(name = "user_agreements")
@Entity
public class UserAgreement extends BaseTimeEntity{
    @EmbeddedId
    @Column(name = "user_agreement_id")
    private UserAgreementId id;        //회원가입 약관 고유 ID

    @Column(name = "is_agreed", nullable = false)
    @ColumnDefault("false")
    private Boolean isAgreed;       //약관 동의 여부

    @Builder
    public UserAgreement(final UserAgreementId id, final Boolean isAgreed){
        this.id = id;
        this.isAgreed = isAgreed;
    }

}
