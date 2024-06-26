package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Category;
import co.orange.ddanzi.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT MAX(CAST(SUBSTRING(p.id, 4, 4) AS integer)) FROM Product p WHERE p.leafCategory = :leafCategory")
    Integer findMaxSequenceNumberByCategory(@Param("leafCategory") Category leafCategory);


    @Query("SELECT p FROM Product p WHERE p.kakaoProductId = :kakaoProductId")
    Product findByKakaoProductId(Long kakaoProductId);

    @Query(value = "SELECT * FROM products p WHERE p.stock > :stock", nativeQuery = true)
    List<Product> findAllByStock(Integer stock);

    @Query(value = "SELECT * FROM products p WHERE p.stock > 0 AND p.name LIKE %:keyword% ", nativeQuery = true)
    List<Product> findAllByName(@Param("keyword") String keyword);

}
