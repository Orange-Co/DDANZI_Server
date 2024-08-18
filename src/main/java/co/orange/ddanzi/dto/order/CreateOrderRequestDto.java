package co.orange.ddanzi.dto.order;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.enums.OrderStatus;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.OptionDetail;
import co.orange.ddanzi.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateOrderRequestDto {
    private String itemId;
    private Long paymentId;
    private Long selectedOptionDetailId;

    public Order toOrder(String orderId, User buyer, Item item, OptionDetail optionDetail) {
        return Order.builder()
                .id(orderId)
                .buyer(buyer)
                .item(item)
                .selectedOptionDetail(optionDetail)
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.ORDER_PLACE)
                .build();
    }
}
