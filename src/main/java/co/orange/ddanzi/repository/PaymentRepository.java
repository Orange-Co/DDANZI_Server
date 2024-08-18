package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p from Payment p WHERE p.buyer = :buyer and p.item = :item and p.payStatus = 'PAID'")
    Payment findByBuyerAndItem(@Param("buyer") User buyer, @Param("item")Item item);
}
