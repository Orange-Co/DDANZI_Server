package co.orange.ddanzi.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageInfo {
    private Long totalElements;
    private Integer numberOfElements;
}
