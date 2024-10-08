package co.orange.ddanzi.dto.common;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OptionInfo {
    private Long optionId;
    private String type;
    private List<OptionDetailInfo> optionDetailList;
}
