package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.user.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
}
