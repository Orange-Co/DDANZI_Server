package co.orange.ddanzi.dto.mypage;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyItemResponseDto {
    private Integer totalCount;
    private List<MyItem> itemProductList;
}
