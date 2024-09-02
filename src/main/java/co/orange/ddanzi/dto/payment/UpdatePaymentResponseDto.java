package co.orange.ddanzi.dto.payment;

import co.orange.ddanzi.domain.order.enums.PayStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdatePaymentResponseDto {
    private String orderId;
    private PayStatus payStatus;
    private LocalDateTime endedAt;
}
