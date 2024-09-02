package co.orange.ddanzi.dto.order;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveOrderResponseDto {
    private String orderId;
    private Enum orderStatus;
}
