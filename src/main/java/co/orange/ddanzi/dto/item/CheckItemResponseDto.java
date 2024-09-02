package co.orange.ddanzi.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckItemResponseDto {
    private String productId;
    private String productName;
    private String imgUrl;
}
