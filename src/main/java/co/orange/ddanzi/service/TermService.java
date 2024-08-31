package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.OrderAgreement;
import co.orange.ddanzi.domain.order.pk.OrderAgreementId;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.ItemAgreement;
import co.orange.ddanzi.domain.product.pk.ItemAgreementId;
import co.orange.ddanzi.domain.term.TermItem;
import co.orange.ddanzi.domain.term.TermJoin;
import co.orange.ddanzi.domain.term.TermOrder;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.UserAgreement;
import co.orange.ddanzi.domain.user.pk.UserAgreementId;
import co.orange.ddanzi.repository.term.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TermService {
    private final TermJoinRepository termJoinRepository;
    private final TermItemRepository termItemRepository;
    private final TermOrderRepository termOrderRepository;

    private final UserAgreementRepository userAgreementRepository;
    private final ItemAgreementsRepository itemAgreementsRepository;
    private final OrderAgreementRepository orderAgreementRepository;

    public void createUserAgreements(User user, Boolean isAgreedMarketingTerm){
        createUserAgreement(user, 1L, true);
        createUserAgreement(user, 2L, true);
        createUserAgreement(user, 3L, isAgreedMarketingTerm);
    }
    public void createItemAgreements(Item item){
        createItemAgreement(item, 1L);
        createItemAgreement(item, 2L);
    }

    public void createOrderAgreements(Order order){
        createOrderAgreement(order, 1L);
        createOrderAgreement(order, 2L);
    }

    public void createUserAgreement(User user, Long termJoinId, boolean isAgreed){
        TermJoin termJoin =  termJoinRepository.findById(termJoinId).orElseThrow(()->new RuntimeException("Term join not found"));

        UserAgreementId userAgreementId = new UserAgreementId();
        userAgreementId.setUser(user); userAgreementId.setTermJoin(termJoin);
        log.info("Set user agreements id");

        UserAgreement userAgreement = UserAgreement.builder()
                .id(userAgreementId)
                .isAgreed(isAgreed).build();

        userAgreementRepository.save(userAgreement);
        log.info("---Create userAgreement---");
    }

    public void createItemAgreement(Item item, Long termItemId){
        log.info("Creating item agreements for term order {}", termItemId);
        TermItem termItem = termItemRepository.findById(termItemId).orElseThrow(()->new RuntimeException("Term item not found"));

        ItemAgreementId itemAgreementId = new ItemAgreementId();
        itemAgreementId.setItem(item); itemAgreementId.setTermItem(termItem);
        log.info("Set item agreements id");

        ItemAgreement itemAgreement = ItemAgreement.builder()
                .id(itemAgreementId)
                .isAgreed(true)
                .build();

        itemAgreementsRepository.save(itemAgreement);
        log.info("---Create item Agreement---");
    }

    public void createOrderAgreement(Order order, Long termOrderId){
        log.info("Creating order agreements for term order {}", termOrderId);
        TermOrder termOrder = termOrderRepository.findById(termOrderId).orElseThrow(()->new RuntimeException("Term order not found"));

        OrderAgreementId orderAgreementId = new OrderAgreementId();
        orderAgreementId.setOrder(order);
        orderAgreementId.setTermOrder(termOrder);
        log.info("Set order agreements id");

        OrderAgreement orderAgreement = OrderAgreement.builder()
                .id(orderAgreementId)
                .isAgreed(true)
                .build();

        orderAgreementRepository.save(orderAgreement);
        log.info("---Create orderAgreement---");
    }

}
