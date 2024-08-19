package co.orange.ddanzi.dto.order;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.enums.OrderStatus;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CreateOrderRequestDto {
    private String itemId;
    private Long paymentId;
    private List<Long> selectedOptionDetailIdList;

    public Order toOrder(String orderId, User buyer, Item item) {
        return Order.builder()
                .id(orderId)
                .buyer(buyer)
                .item(item)
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.ORDER_PLACE)
                .build();
    }
}
