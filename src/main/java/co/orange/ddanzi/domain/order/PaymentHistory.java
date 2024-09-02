package co.orange.ddanzi.domain.order;


import co.orange.ddanzi.domain.order.enums.PayStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "payment_histories")
@Entity
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_history_id")
    private Long id;

    @Column(name = "buyer_id")
    private Long buyerId;

    @Column(name = "pay_status")
    private PayStatus payStatus;

    @Column(name = "error")
    private String error;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column
    private LocalDateTime createAt;

    @Builder
    public PaymentHistory(Long buyerId, PayStatus payStatus, String error, Payment payment, LocalDateTime createAt) {
        this.buyerId = buyerId;
        this.payStatus = payStatus;
        this.error = error;
        this.payment = payment;
        this.createAt = createAt;
    }
}
