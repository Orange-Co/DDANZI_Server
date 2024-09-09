package co.orange.ddanzi.domain.user;

import co.orange.ddanzi.domain.order.Order;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_case")
    private FcmCase alarmCase;

    @Column(name = "is_checked")
    private Boolean isChecked;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    @Builder
    public Alarm(FcmCase alarmCase, User user, Order order) {
        this.alarmCase = alarmCase;
        this.user = user;
        this.isChecked = false;
        this.createdAt = LocalDateTime.now();
        this.order = order;
    }

    public void checkAlarm(Boolean isChecked){
        this.isChecked = isChecked;
    }
}
