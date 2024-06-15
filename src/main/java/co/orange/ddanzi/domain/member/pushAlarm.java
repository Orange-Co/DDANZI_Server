package co.orange.ddanzi.domain.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    private Member member;          //member id (PK)

    @Column(name = "is_allowed", nullable = false)
    @ColumnDefault("false")
    private Boolean isAllowed;      //푸시 알람 허용 여부 (default false)

    @Builder
    public pushAlarm(Member member, Boolean isAllowed) {
        this.member = member;
        this.isAllowed = isAllowed;
    }
}
