package co.orange.ddanzi.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SelectedOption {
    private String option;
    private String selectedOption;
}
