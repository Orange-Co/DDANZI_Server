package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
