package co.orange.ddanzi.dto.item;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ItemAddressOptionResponseDto {
    private String address;
    private String detailAddress;
    private String zipCode;
    private String recipient;
    private String recipientPhone;
    private List<SelectedOption> selectedOptionList;
}
