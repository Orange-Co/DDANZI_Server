package co.orange.ddanzi.dto.item;

import lombok.Getter;

@Getter
public class ConfirmProductRequestDto {
    private Long kakaoProductId;
    private String productName;
    private String category;
}
