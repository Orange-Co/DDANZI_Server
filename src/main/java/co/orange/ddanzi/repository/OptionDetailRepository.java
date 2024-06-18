package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.OptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OptionDetailRepository extends JpaRepository<OptionDetail, Long> {
    @Query(value = "SELECT * from option_details od WHERE od.option_id = :optionId", nativeQuery = true)
    List<OptionDetail> findAllByOptionId(Long optionId);
}
