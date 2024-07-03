package co.orange.ddanzi.dto.home;

import co.orange.ddanzi.domain.product.enums.OptionType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OptionInfo {
    private Long optionId;
    private OptionType type;
    private List<OptionDetailInfo> optionDetailList;
}
