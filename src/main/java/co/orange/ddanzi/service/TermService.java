package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.OrderAgreement;
import co.orange.ddanzi.domain.order.pk.OrderAgreementId;
import co.orange.ddanzi.domain.term.TermOrder;
import co.orange.ddanzi.repository.term.OrderAgreementRepository;
import co.orange.ddanzi.repository.term.TermOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TermService {
    private final TermOrderRepository termOrderRepository;
    private final OrderAgreementRepository orderAgreementRepository;

    public void createOrderAgreement(Order order, Long termOrderId, Boolean isAgreed){
        log.info("Creating order agreements for term order {}", termOrderId);
        TermOrder termOrder = termOrderRepository.findById(termOrderId).orElseThrow(()->new RuntimeException("Term order not found"));

        OrderAgreementId orderAgreementId = new OrderAgreementId();
        orderAgreementId.setOrder(order); orderAgreementId.setTermOrder(termOrder);
        orderAgreementId.setTermOrder(termOrder);
        log.info("Set order agreements id");

        OrderAgreement orderAgreement = OrderAgreement.builder()
                .id(orderAgreementId)
                .isAgreed(isAgreed)
                .build();

        orderAgreementRepository.save(orderAgreement);
        log.info("---Create orderAgreement---");
    }

}
