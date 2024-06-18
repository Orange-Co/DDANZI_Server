package co.orange.ddanzi.dto.home;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HomeResponseDto {
    private List<ProductInfo> productList;
}
