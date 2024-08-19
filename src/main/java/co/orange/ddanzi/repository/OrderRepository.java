package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByBuyer(User user);
}