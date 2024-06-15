package co.orange.ddanzi.domain.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class pushAlarm {
    @Id
    @Column(name = "member_id")
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Boolean isAllowed;

    @Builder
    public pushAlarm(Member member, Boolean isAllowed) {
        this.member = member;
        this.isAllowed = isAllowed;
    }
}
