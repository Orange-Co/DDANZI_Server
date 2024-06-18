package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM products p WHERE p.stock > :stock", nativeQuery = true)
    List<Product> findAllByStock(Integer stock);
}
