package co.orange.ddanzi.dto.home;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HomeDetailResponseDto {
    private String name;
    private String imgUrl;
    private String category;
    private Boolean isOptionExist;
    private Boolean isImminent;
    private Integer discountRate;
    private Integer originPrice;
    private Integer salePrice;
    private Integer stockCount;
    private String infoUrl;
    private Boolean isInterested;
    private Integer interestCount;
    private List<OptionInfo> optionList;
}
