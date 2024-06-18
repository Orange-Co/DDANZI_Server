package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT c.* FROM categories c " +
            "JOIN products p ON p.category_id = c.category_id " +
            "LEFT JOIN categories cc ON c.category_id = cc.parent_category_id " +
            "WHERE p.product_id = :productId " +
            "AND cc.category_id IS NULL", nativeQuery = true)
    Optional<Category> findLeafCategoryByProductId(@Param("productId") Long productId);


}
