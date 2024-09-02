package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.order.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
}
