package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, String> {
}
