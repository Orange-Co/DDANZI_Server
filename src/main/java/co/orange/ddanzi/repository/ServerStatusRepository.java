package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.ServerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServerStatusRepository extends JpaRepository<ServerStatus, Integer> {
    @Query(value = "SELECT s FROM ServerStatus s ORDER BY s.id DESC")
    ServerStatus findTopByOrderByIdDesc();
}
