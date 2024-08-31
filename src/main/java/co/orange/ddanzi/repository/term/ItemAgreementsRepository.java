package co.orange.ddanzi.repository.term;

import co.orange.ddanzi.domain.product.ItemAgreement;
import co.orange.ddanzi.domain.product.pk.ItemAgreementId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAgreementsRepository extends JpaRepository<ItemAgreement, ItemAgreementId> {
}
