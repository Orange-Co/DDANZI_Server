package co.orange.ddanzi.dto.home;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionDetailInfo {
    private Long optionDetailId;
    private String content;
    private Boolean isAvailable;
}
