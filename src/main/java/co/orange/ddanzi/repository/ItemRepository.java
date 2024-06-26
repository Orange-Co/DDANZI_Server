package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.product.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
}
