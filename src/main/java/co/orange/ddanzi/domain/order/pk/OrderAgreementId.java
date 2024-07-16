package co.orange.ddanzi.domain.order.pk;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.term.TermOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class OrderAgreementId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_order_id")
    private TermOrder termOrder;
}
