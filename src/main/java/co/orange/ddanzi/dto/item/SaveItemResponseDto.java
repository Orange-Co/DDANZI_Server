package co.orange.ddanzi.dto.item;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class SaveItemResponseDto {
    private UUID itemId;
    private String productName;
    private Integer originPrice;
}
