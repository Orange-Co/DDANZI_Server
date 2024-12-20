package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, String> {
    @Query("SELECT i FROM Item i WHERE i.seller = :user AND i.status <> 'DELETED'")
    List<Item> findAllBySellerAndNotDeleted(User user);


    @Query("SELECT MAX(CAST(RIGHT(i.id, 2) AS integer)) FROM Item i WHERE i.product = :product")
    Integer findMaxSequenceNumberByProduct(@Param("product") Product product);

    @Query("SELECT i FROM Item i WHERE i.status = 'ON_SALE' AND i.product = :product ORDER BY i.dueDate ASC")
    List<Item> findNearestExpiryItems(@Param("product") Product product);

    default Optional<Item> findNearestExpiryItem(@Param("product") Product product) {
        List<Item> items = findNearestExpiryItems(product);
        return items.isEmpty() ? Optional.empty() : Optional.of(items.get(0));
    }

    @Query("SELECT i FROM Item  i WHERE i.dueDate < :currentDate AND i.status = 'ON_SALE'" )
    List<Item> findOnSaleExpiryItems(@Param("currentDate") LocalDate currentDate);
}
