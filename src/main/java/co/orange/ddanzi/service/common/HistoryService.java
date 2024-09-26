package co.orange.ddanzi.service.common;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.OrderHistory;
import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.order.PaymentHistory;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.repository.OrderHistoryRepository;
import co.orange.ddanzi.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class HistoryService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    public void createOrderHistory(Order order){
        log.info("Creating order history, order status : ", order.getStatus());
        OrderHistory newOrderHistory = OrderHistory.builder()
                .orderStatus(order.getStatus())
                .createdAt(LocalDateTime.now())
                .order(order)
                .build();
        orderHistoryRepository.save(newOrderHistory);
    }

    public void createPaymentHistory(User user, Payment payment){
        PaymentHistory paymentHistory = PaymentHistory.builder()
                .buyerId(user.getId())
                .payStatus(payment.getPayStatus())
                .payment(payment)
                .createAt(LocalDateTime.now())
                .build();
        paymentHistoryRepository.save(paymentHistory);
    }

    public void createPaymentHistoryWithError(User user, Payment payment, String error){
        PaymentHistory paymentHistory = PaymentHistory.builder()
                .buyerId(user.getId())
                .payStatus(payment.getPayStatus())
                .payment(payment)
                .error(error)
                .createAt(LocalDateTime.now())
                .build();
        paymentHistoryRepository.save(paymentHistory);
    }
}
