package co.orange.ddanzi.dto.home;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductInfo {
    private Long productId;
    private String name;
    private String imgUrl;
    private Integer originPrice;
    private Integer salePrice;
    private Integer interestCount;
}