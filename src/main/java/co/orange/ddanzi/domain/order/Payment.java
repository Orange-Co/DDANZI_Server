package co.orange.ddanzi.domain.order;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = "payments")
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "pay_status")
    private String payStatus;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder
    public Payment(String payStatus, LocalDateTime completedAt) {
        this.payStatus = payStatus;
        this.completedAt = completedAt;
    }
}
