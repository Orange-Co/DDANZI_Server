package co.orange.ddanzi.domain.order.pk;

import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.term.TermOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class OrderAgreementId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_order_id")
    private TermOrder termOrder;
}
