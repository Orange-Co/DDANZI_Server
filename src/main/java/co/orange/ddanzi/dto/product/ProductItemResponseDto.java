package co.orange.ddanzi.dto.product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductItemResponseDto {
    private String productId;
    private String productName;
    private Integer originPrice;
    private Integer salePrice;
    private Boolean isAccountExist;
}
