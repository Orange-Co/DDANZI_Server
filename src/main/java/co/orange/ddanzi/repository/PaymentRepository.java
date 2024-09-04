package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrder(Order order);
}
