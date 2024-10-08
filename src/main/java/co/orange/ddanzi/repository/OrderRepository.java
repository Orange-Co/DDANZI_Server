package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.enums.OrderStatus;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o from Order o where o.buyer  = :user AND o.status <>'ORDER_PENDING'")
    List<Order> findByBuyerAndStatus(User user);

    @Query("SELECT o FROM Order o WHERE o.item = :item AND o.status <> 'ORDER_PENDING'")
    Optional<Order> findByItemAndStatus(@Param("item") Item item);

    @Query("SELECT o FROM Order o WHERE o.status = :orderStatus AND o.updatedAt <= :dateTime")
    List<Order> findOverLimitTimeOrders(OrderStatus orderStatus, LocalDateTime dateTime);
}