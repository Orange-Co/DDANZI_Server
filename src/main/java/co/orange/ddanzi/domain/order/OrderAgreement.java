package co.orange.ddanzi.domain.order;

import co.orange.ddanzi.domain.order.pk.OrderAgreementId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;


@NoArgsConstructor
@Table(name = "order_agreements")
@Entity
public class OrderAgreement {
    @EmbeddedId
    @Column(name = "order_agreement_id")
    private OrderAgreementId id;

    @Column(name = "is_agreed", nullable = false)
    @ColumnDefault("false")
    private Boolean isAgreed;

    @Builder
    public OrderAgreement(OrderAgreementId id, Boolean isAgreed) {
        this.id = id;
        this.isAgreed = isAgreed;
    }
}
