package co.orange.ddanzi.dto.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundRequestDto {
    private String merchant_uid;
    private String reason;
}
