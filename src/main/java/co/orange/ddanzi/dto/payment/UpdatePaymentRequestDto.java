package co.orange.ddanzi.dto.payment;

import co.orange.ddanzi.domain.order.enums.PayStatus;
import lombok.Getter;

@Getter
public class UpdatePaymentRequestDto {
    private String orderId;
    private PayStatus payStatus;
}
