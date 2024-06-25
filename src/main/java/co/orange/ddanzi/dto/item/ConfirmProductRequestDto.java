package co.orange.ddanzi.dto.item;

import co.orange.ddanzi.domain.product.Category;
import co.orange.ddanzi.domain.product.Product;
import lombok.Getter;

@Getter
public class ConfirmProductRequestDto {
    private Long kakaoProductId;
    private String productName;
    private Integer originPrice;
    private String imgUrl;
    private String infoUrl;
    private String category;
    private Boolean isForbidden;

    public Category toCategory(String content){
        return Category.builder()
                .content(content)
                .isForbidden(isForbidden)
                .build();
    }

    public Product toProduct(Integer discountPrice){
        return Product.builder()
                .kakaoProductId(kakaoProductId)
                .name(productName)
                .originPrice(originPrice)
                .discountPrice(discountPrice)
                .imgUrl(imgUrl)
                .infoUrl(infoUrl)
                .stock(0)
                .build();
    }
}
