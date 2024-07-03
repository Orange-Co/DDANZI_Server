package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
