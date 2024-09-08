package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.user.PushAlarm;
import co.orange.ddanzi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PushAlarmRepository extends JpaRepository<PushAlarm, Long> {
    @Query("SELECT pa FROM PushAlarm pa WHERE pa.user = :user ")
    Optional<PushAlarm> findByUser(@Param("user") User user);
}
