package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
