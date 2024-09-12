package co.orange.ddanzi.dto.item;


import co.orange.ddanzi.domain.order.enums.PayMethod;
import co.orange.ddanzi.dto.common.AddressInfo;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@Builder
public class ItemResponseDto {
    private String itemId;
    private String imgUrl;
    private String status;
    private String productName;
    private Integer originPrice;
    private Integer salePrice;
    private String orderId;
    private String buyerNickName;
    private AddressInfo addressInfo;
    private LocalDateTime paidAt;
    private String paymentMethod;
}
