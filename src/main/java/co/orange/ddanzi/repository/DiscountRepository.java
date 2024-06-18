package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Query(value = "SELECT * FROM discounts d WHERE d.category_id = :categoryId",nativeQuery = true)
    Discount findByCategoryId(@Param("categoryId") Long categoryId);
}
