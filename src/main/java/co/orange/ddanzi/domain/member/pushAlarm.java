package co.orange.ddanzi.domain.member;

import jakarta.persistence.*;

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
}
