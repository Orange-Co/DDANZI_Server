package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Category;
import co.orange.ddanzi.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT MAX(CAST(SUBSTRING(p.id, 4, 4) AS integer)) FROM Product p WHERE p.leafCategory = :leafCategory")
    Integer findMaxSequenceNumberByCategory(@Param("leafCategory") Category leafCategory);

    @Query("SELECT p FROM Product p WHERE p.kakaoProductId = :kakaoProductId")
    Product findByKakaoProductId(Long kakaoProductId);

    @Query(value = "SELECT * FROM products p WHERE p.stock > :stock ORDER BY p.closest_due_date asc ", nativeQuery = true)
    Page<Product> findAllByStockAAndClosestDueDate(Pageable pageable, Integer stock);

    @Query(value = "SELECT * FROM products p WHERE p.stock > 0 AND p.name LIKE %:keyword% ", nativeQuery = true)
    List<Product> findAllByName(@Param("keyword") String keyword);

    List<Product> findByIdIn(List<String> productIds);
}
