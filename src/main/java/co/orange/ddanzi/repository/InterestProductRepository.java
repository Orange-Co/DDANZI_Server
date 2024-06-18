package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.InterestProduct;
import co.orange.ddanzi.domain.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterestProductRepository extends JpaRepository<InterestProduct, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM InterestProduct ip WHERE ip.id.user = :user AND ip.id.product = :product")
    void deleteByUserAndProduct(@Param("user") User user, @Param("product") Product product);
}
