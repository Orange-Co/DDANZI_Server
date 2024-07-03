package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.content = :content AND c.parentCategory = :parentCategory")
    Optional<Category> findByContentAndParentCategory(@Param("content") String content, @Param("parentCategory") Category parentCategory);

    @Query("SELECT c FROM Category c WHERE c.content = :content")
    Optional<Category> findByContent(String content);

}
