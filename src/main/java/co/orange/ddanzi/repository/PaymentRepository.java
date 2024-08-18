package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.order.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
