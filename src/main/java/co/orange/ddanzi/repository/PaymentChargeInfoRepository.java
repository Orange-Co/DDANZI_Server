package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.order.PaymentChargeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentChargeInfoRepository extends JpaRepository<PaymentChargeInfo, Integer> {
}
