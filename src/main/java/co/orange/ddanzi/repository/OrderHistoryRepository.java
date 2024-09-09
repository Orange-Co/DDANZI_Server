package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.order.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
}
