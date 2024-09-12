package co.orange.ddanzi.dto.home;

import co.orange.ddanzi.dto.common.PageInfo;
import co.orange.ddanzi.dto.common.ProductInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HomeResponseDto {
    private String homeImgUrl;
    private PageInfo pageInfo;
    private List<ProductInfo> productList;
}
