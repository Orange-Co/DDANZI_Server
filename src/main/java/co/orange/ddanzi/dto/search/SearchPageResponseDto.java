package co.orange.ddanzi.dto.search;

import co.orange.ddanzi.dto.common.ProductInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchPageResponseDto {
    private List<String> topSearchedList;
    private List<ProductInfo> recentlyViewedList;
}
