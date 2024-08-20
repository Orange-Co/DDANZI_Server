package co.orange.ddanzi.dto.mypage;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyOrder {
    private String productId;
    private String orderId;
    private String productName;
    private String imgUrl;
    private Integer originPrice;
    private Integer salePrice;
    private LocalDateTime paidAt;
}
