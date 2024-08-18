package co.orange.ddanzi.dto.order;

import lombok.Getter;

@Getter
public class CreateOrderRequestDto {
    private String itemId;
    private String paymentId;
    private Long selectedOptionDetailId;
    private Boolean orderTerm1;
    private Boolean orderTerm2;
    private Boolean orderTerm3;
}
