package co.orange.ddanzi.domain.product;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Table(name = "products")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;        //상품 고유 ID

    @Column(name = "name", nullable = false)
    private String name;    //상품명

    @Column(name = "origin_price", nullable = false)
    private Integer originPrice;    //정가=선물하기 가격

    @Column(name = "discount_price", nullable = false)
    private Integer discountPrice;  //할인 금액

    @ColumnDefault("0")
    @Column(name = "stock", nullable = false)
    private Integer stock;          //재고 수

    @Column(name = "img_url")
    private String imgUrl;          //상품 이미지

    @Column(name = "info_url")
    private String infoUrl;         //상품 상세 정보 url

    @ColumnDefault("0")
    @Column(name = "interest_count", nullable = false)
    private Integer interestCount;  //찜 수

    @Builder
    public Product (String name, Integer originPrice, Integer discountPrice, String imgUrl, String infoUrl, Integer stock, Integer interestCount) {
        this.name = name;
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.imgUrl = imgUrl;
        this.infoUrl = infoUrl;
        this.stock = stock;
        this.interestCount = interestCount;
    }
}
