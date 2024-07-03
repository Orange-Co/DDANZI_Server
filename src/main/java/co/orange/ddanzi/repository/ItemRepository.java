package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, String> {
    @Query("SELECT MAX(CAST(SUBSTRING(i.id, 14, 2) AS integer)) FROM Item i WHERE i.product = :product")
    Integer findMaxSequenceNumberByProduct(@Param("product") Product product);

}
