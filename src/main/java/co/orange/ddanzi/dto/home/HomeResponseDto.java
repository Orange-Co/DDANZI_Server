package co.orange.ddanzi.dto.home;

import co.orange.ddanzi.dto.ProductInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HomeResponseDto {
    private String homeImgUrl;
    private List<ProductInfo> productList;
}
