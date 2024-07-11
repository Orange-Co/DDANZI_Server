package co.orange.ddanzi.dto.order;

import co.orange.ddanzi.dto.AddressInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckProductResponseDto {
    private String productName;
    private String imgUrl;
    private Integer originPrice;
    private AddressInfo addressInfo;
    private Integer discountPrice;
    private Integer charge;
    private Integer totalPrice;
}
