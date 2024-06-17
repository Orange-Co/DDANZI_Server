package co.orange.ddanzi.domain.product;

import co.orange.ddanzi.common.domain.BaseTimeEntity;
import co.orange.ddanzi.domain.product.pk.ItemTermId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Entity
public class ItemTerm extends BaseTimeEntity {
    @EmbeddedId
    @Column(name = "item_term_id")
    private ItemTermId id;

    @Column(name = "is_agreed", nullable = false)
    @ColumnDefault("false")
    private Boolean isAgreed;

    @Builder
    public ItemTerm(ItemTermId id, Boolean isAgreed) {
        this.id = id;
        this.isAgreed = isAgreed;
    }
}
