package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.user.Address;
import co.orange.ddanzi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE a.user = :user")
    Address findByUser(User user);
}
