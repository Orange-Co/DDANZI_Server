package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.user.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Integer> {
    Bank findByBankCode(String bankCode);
}
