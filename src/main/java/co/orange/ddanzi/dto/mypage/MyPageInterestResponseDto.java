package co.orange.ddanzi.dto.mypage;

import co.orange.ddanzi.dto.common.ProductInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPageInterestResponseDto {
    private Integer totalCount;
    private List<ProductInfo> productList;
}
