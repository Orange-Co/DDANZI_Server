package co.orange.ddanzi.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConfirmProductResponseDto {
    private Long productId;
    private String productName;
    private Integer originPrice;
    private Integer salePrice;
}
