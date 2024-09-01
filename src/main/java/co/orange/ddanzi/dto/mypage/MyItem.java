package co.orange.ddanzi.dto.mypage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyItem {
    private String productId;
    private String itemId;
    private String productName;
    private String imgUrl;
    private Integer originPrice;
    private Integer salePrice;
    private Boolean isInterested;
    private Integer interestCount;
}
