package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.OrderHistory;
import co.orange.ddanzi.repository.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class HistoryService {
    private final OrderHistoryRepository orderHistoryRepository;

    public void createOrderHistory(Order order){
        log.info("Creating order history, order status : ", order.getStatus());
        OrderHistory newOrderHistory = OrderHistory.builder()
                .orderStatus(order.getStatus())
                .createdAt(LocalDateTime.now())
                .order(order)
                .build();
        orderHistoryRepository.save(newOrderHistory);
    }
}
