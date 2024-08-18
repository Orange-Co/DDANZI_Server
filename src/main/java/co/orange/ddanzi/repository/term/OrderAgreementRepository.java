package co.orange.ddanzi.repository.term;

import co.orange.ddanzi.domain.order.OrderAgreement;
import co.orange.ddanzi.domain.order.pk.OrderAgreementId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderAgreementRepository extends JpaRepository<OrderAgreement, OrderAgreementId> {
}