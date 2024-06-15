package co.orange.ddanzi.domain.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;        //상품 고유 ID

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "origin_price", nullable = false)
    private Integer originPrice;

    @Column(name = "discount_price", nullable = false)
    private Integer discountPrice;

    @ColumnDefault("0")
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "info_url")
    private String infoUrl;

    @ColumnDefault("0")
    @Column(name = "interest_count", nullable = false)
    private Integer interestCount;
}
