package co.orange.ddanzi.domain.user;

import co.orange.ddanzi.domain.user.enums.FcmCase;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = "alarms")
@Entity
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "alarm_case")
    private FcmCase alarmCase;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Builder
    public Alarm(FcmCase alarmCase, String content, User user) {
        this.alarmCase = alarmCase;
        this.content = content;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
}
