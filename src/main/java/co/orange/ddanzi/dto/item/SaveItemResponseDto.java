package co.orange.ddanzi.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveItemResponseDto {
    private String itemId;
    private String productName;
    private Integer originPrice;
}
