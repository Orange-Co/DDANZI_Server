package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.user.Account;
import co.orange.ddanzi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.user = :user")
    Account findByUserId(User user);

    boolean existsByNumber(String number);
}

