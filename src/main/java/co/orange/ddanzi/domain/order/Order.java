package co.orange.ddanzi.domain.order;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.order.enums.OrderStatus;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.OptionDetail;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Table(name = "orders")
@Entity
public class Order {
    @Id
    @Column(name = "order_id")
    private String id;                        //주문 고유 ID = UUID

    @Column(name = "charge")
    private Integer charge;                 //수수료

    @Column(name = "total_price")
    private Integer totalPrice;             //최종 금액

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ORDER_PLACE'")
    @Column(name = "status", nullable = false)
    private OrderStatus status;             //주문 상태(입금완료/배송중/거래완료/거래취소)

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;        //주문일자

    @Column(name = "completed_at")
    private LocalDateTime completedAt;      //거래완료 일자

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;                      //판매 제품

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;                   //구매자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_detail_id")
    private OptionDetail selectedOptionDetail;      //선택된 옵션


    @Builder
    public Order(String id, Integer charge, Integer totalPrice, OrderStatus status, LocalDateTime createdAt, LocalDateTime completedAt, Item item, User buyer, OptionDetail selectedOptionDetail) {
        this.id = id;
        this.charge = charge;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.item = item;
        this.buyer = buyer;
        this.selectedOptionDetail = selectedOptionDetail;

    }
}
