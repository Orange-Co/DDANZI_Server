package co.orange.ddanzi.dto.search;

import co.orange.ddanzi.dto.ProductInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchResultResponseDto {
    private List<String> topSearchedList;
    private List<ProductInfo> searchedProductList;
}
