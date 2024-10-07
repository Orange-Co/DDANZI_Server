package co.orange.ddanzi.dto.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundRequestDto {
    private Integer amount;
    private Integer taxFreeAmount;
    private String reason;
}
