package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.user.Alarm;
import co.orange.ddanzi.domain.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Query("SELECT a FROM Alarm a WHERE a.user = :user AND a.createdAt >= :sevenDaysAgo ORDER BY a.createdAt DESC")
    List<Alarm> findRecentAlarmsByUser(@Param("user") User user, @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);
}
