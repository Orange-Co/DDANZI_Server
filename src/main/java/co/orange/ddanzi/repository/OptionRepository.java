package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    @Query(value = "SELECT * FROM options o WHERE o.product_id = :productId", nativeQuery = true)
    List<Option> findAllByProductId(String productId);
}
