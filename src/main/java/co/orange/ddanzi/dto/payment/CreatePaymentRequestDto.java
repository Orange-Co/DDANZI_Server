package co.orange.ddanzi.dto.payment;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.order.enums.OrderStatus;
import co.orange.ddanzi.domain.order.enums.PayMethod;
import co.orange.ddanzi.domain.order.enums.PayStatus;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreatePaymentRequestDto {
    private String productId;
    private Integer charge;
    private Integer totalPrice;
    private PayMethod method;

    public Payment toEntity(Order order){
        return Payment.builder()
                .order(order)
                .charge(charge)
                .totalPrice(totalPrice)
                .method(method)
                .payStatus(PayStatus.PENDING)
                .startedAt(LocalDateTime.now())
                .endedAt(null)
                .build();
    }
}
