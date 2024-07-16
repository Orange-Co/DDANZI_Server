package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.DefaultDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DefaultDiscountRepository extends JpaRepository<DefaultDiscount, Long> {
    @Query(value = "SELECT * FROM discounts d WHERE d.category_id = :categoryId",nativeQuery = true)
    DefaultDiscount findByCategoryId(@Param("categoryId") Long categoryId);
}
