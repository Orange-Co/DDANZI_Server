package co.orange.ddanzi.domain.product.pk;

import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.term.TermItem;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ItemAgreementId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_item_id")
    private TermItem termItem;

}
