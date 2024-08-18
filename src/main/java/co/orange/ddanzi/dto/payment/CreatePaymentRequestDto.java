package co.orange.ddanzi.dto.payment;

import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.order.enums.PayMethod;
import co.orange.ddanzi.domain.order.enums.PayStatus;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreatePaymentRequestDto {
    private String itemId;
    private Integer charge;
    private Integer totalPrice;
    private PayMethod method;

    public Payment toEntity(Item item, User buyer){
        return Payment.builder()
                .charge(charge)
                .totalPrice(totalPrice)
                .method(method)
                .payStatus(PayStatus.PENDING)
                .startedAt(LocalDateTime.now())
                .endedAt(null)
                .item(item)
                .buyer(buyer)
                .build();
    }
}
