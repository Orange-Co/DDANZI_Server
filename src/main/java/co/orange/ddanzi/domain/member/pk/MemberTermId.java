package co.orange.ddanzi.domain.member.pk;

import co.orange.ddanzi.domain.member.Member;
import co.orange.ddanzi.domain.term.TermJoin;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class MemberTermId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_join_id")
    private TermJoin termJoin;

}
