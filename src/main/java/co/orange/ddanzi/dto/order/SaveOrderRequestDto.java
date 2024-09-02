package co.orange.ddanzi.dto.order;

import lombok.Getter;

import java.util.List;

@Getter
public class SaveOrderRequestDto {
    private String orderId;
    private List<Long> selectedOptionDetailIdList;

}
