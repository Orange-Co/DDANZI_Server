package co.orange.ddanzi.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductInfo {
    private String productId;
    private Long kakaoProductId;
    private String name;
    private String imgUrl;
    private Integer originPrice;
    private Integer salePrice;
    private Integer interestCount;
    private Boolean isInterested;
}
