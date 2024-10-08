package co.orange.ddanzi.domain.order;

import co.orange.ddanzi.domain.order.enums.PayMethod;
import co.orange.ddanzi.domain.order.enums.PayStatus;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Or;

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

    @Column(name = "charge")
    private Integer charge;                 //수수료

    @Column(name = "total_price")
    private Integer totalPrice;             //최종 금액

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PayMethod method;               //결제 수단

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status")
    private PayStatus payStatus;               //결제 상태

    @Column(name = "started_at")
    private LocalDateTime startedAt;        //결제 시작

    @Column(name = "ended_at")
    private LocalDateTime endedAt;          //결제 완료

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;                    //주문 ID

    @Builder
    public Payment(Integer charge, Integer totalPrice, PayMethod method, PayStatus payStatus, LocalDateTime startedAt, LocalDateTime endedAt, Order order) {
        this.charge = charge;
        this.totalPrice = totalPrice;
        this.method = method;
        this.payStatus = payStatus;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.order = order;
    }

    public void updatePaymentStatusAndEndedAt(PayStatus newStatus) {
        this.payStatus = newStatus;
        this.endedAt = LocalDateTime.now();
    }
}
