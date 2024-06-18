package co.orange.ddanzi.dto.interest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterestResponseDto {
    private String nickname;
    private Long productId;
}
