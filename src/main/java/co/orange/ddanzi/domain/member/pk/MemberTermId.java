package co.orange.ddanzi.domain.member.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class MemberTermId implements Serializable {
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "term_join_id")
    private Long termJoinId;

}
