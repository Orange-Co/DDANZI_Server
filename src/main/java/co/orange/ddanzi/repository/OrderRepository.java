package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}