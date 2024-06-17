package co.orange.ddanzi.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Table(name = "push_alarms")
@Entity
public class pushAlarm {
    @Id
    @Column(name = "member_id")
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;          //member id (PK)

    @Column(name = "is_allowed", nullable = false)
    @ColumnDefault("false")
    private Boolean isAllowed;      //푸시 알람 허용 여부 (default false)

    @Builder
    public pushAlarm(User user, Boolean isAllowed) {
        this.user = user;
        this.isAllowed = isAllowed;
    }
}
