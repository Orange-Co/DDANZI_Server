package co.orange.ddanzi.domain.product;

import co.orange.ddanzi.global.common.domain.BaseTimeEntity;
import co.orange.ddanzi.domain.product.pk.ItemAgreementId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Table(name = "item_agreements")
@Entity
public class ItemAgreement extends BaseTimeEntity {
    @EmbeddedId
    @Column(name = "item_agreement_id")
    private ItemAgreementId id;

    @Column(name = "is_agreed", nullable = false)
    @ColumnDefault("false")
    private Boolean isAgreed;

    @Builder
    public ItemAgreement(ItemAgreementId id, Boolean isAgreed) {
        this.id = id;
        this.isAgreed = isAgreed;
    }
}
