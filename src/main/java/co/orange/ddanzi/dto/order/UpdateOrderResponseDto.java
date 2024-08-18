package co.orange.ddanzi.dto.order;

import co.orange.ddanzi.domain.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateOrderResponseDto {
    private String orderId;
    private OrderStatus orderStatus;
}
