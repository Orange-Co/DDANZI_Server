package co.orange.ddanzi.dto.order;


import co.orange.ddanzi.domain.order.enums.OrderStatus;
import co.orange.ddanzi.domain.order.enums.PayMethod;
import co.orange.ddanzi.dto.AddressInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponseDto {
    private String orderId;
    private OrderStatus orderStatus;
    private String productName;
    private String imgUrl;
    private Integer originPrice;
    private AddressInfo addressInfo;
    private String sellerNickname;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private Integer discountPrice;
    private Integer charge;
    private Integer totalPrice;
}
