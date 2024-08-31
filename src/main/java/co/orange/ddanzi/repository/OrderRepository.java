package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByBuyer(User user);

    @Query("SELECT o from Order o where o.item  = :item")
    Optional<Order> findByItem(@Param("item") Item item);
}