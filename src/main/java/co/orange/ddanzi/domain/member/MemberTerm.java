package co.orange.ddanzi.domain.member;

import co.orange.ddanzi.common.domain.BaseTimeEntity;
import co.orange.ddanzi.domain.member.pk.MemberTermId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Entity
public class MemberTerm extends BaseTimeEntity{
    @EmbeddedId
    @Column(name = "member_term_id")
    private MemberTermId id;        //회원가입 약관 고유 ID

    @Column(name = "is_agreed", nullable = false)
    @ColumnDefault("false")
    private Boolean isAgreed;       //약관 동의 여부

    @Builder
    public MemberTerm(final MemberTermId id, final Boolean isAgreed){
        this.id = id;
        this.isAgreed = isAgreed;
    }

}
