package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Integer> {
    Banner findByIsSelected(Boolean isSelected);
}
