package co.orange.ddanzi.domain.order;

import co.orange.ddanzi.domain.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "order_histories")
@Entity
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_history_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public OrderHistory(OrderStatus orderStatus, LocalDateTime createdAt, Order order) {
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.order = order;
    }

}
