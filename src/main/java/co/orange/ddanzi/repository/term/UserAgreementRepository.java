package co.orange.ddanzi.repository.term;

import co.orange.ddanzi.domain.user.UserAgreement;
import co.orange.ddanzi.domain.user.pk.UserAgreementId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAgreementRepository extends JpaRepository<UserAgreement, UserAgreementId> {
}
