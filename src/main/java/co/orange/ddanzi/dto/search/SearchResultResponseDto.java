package co.orange.ddanzi.dto.search;

import co.orange.ddanzi.dto.home.ProductInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchResultResponseDto {
    private List<ProductInfo> searchedProductList;
}
