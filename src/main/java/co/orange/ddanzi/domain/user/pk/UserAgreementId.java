package co.orange.ddanzi.domain.user.pk;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.term.TermJoin;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class UserAgreementId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_join_id")
    private TermJoin termJoin;

}
