package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.user.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
