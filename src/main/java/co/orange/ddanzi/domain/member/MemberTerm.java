package co.orange.ddanzi.domain.member;

import co.orange.ddanzi.common.domain.BaseTimeEntity;
import co.orange.ddanzi.domain.member.pk.MemberTermId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class MemberTerm extends BaseTimeEntity{
    @EmbeddedId
    @Column(name = "member_term_id")
    private MemberTermId id;

    @Column(name = "is_agreed", nullable = false)
    private Boolean isAgreed;

    @Builder
    public MemberTerm(final MemberTermId id, final Boolean isAgreed){
        this.id = id;
        this.isAgreed = isAgreed;
    }

}
