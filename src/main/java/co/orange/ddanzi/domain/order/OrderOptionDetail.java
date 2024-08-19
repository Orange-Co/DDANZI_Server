package co.orange.ddanzi.domain.order;

import co.orange.ddanzi.domain.product.OptionDetail;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "orders_option_details")
@Entity
public class OrderOptionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_option_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_detail_id")
    private OptionDetail optionDetail;

    @Builder
    public OrderOptionDetail(Order order, OptionDetail optionDetail) {
        this.order = order;
        this.optionDetail = optionDetail;
    }
}
