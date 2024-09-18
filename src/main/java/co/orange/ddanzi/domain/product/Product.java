package co.orange.ddanzi.domain.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Table(name = "products")
@Entity
public class Product {
    @Id
    @Column(name = "product_id")
    private String id;                  //상품 고유 ID

    @Column(name = "kakao_product_id", nullable = false, unique = true)
    private Long kakaoProductId;        //카카오톡 선물하기 상품 ID

    @Column(name = "name", nullable = false)
    private String name;                //상품명

    @Column(name = "origin_name", nullable = false, unique = true)
    private String originName;          //변경 전 상품명

    @Column(name = "origin_price", nullable = false)
    private Integer originPrice;        //정가=선물하기 가격

    @Column(name = "store")
    private String store;               //가게

    @ColumnDefault("0")
    @Column(name = "stock", nullable = false)
    private Integer stock;              //재고 수

    @Column(name = "img_url")
    private String imgUrl;              //상품 이미지

    @Column(name = "info_url")
    private String infoUrl;             //상품 상세 정보 url

    @Column(name = "closest_due_date")
    private LocalDate closestDueDate;   //가장 가까운 만료일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaf_category_id")
    private Category leafCategory;      //리프 카테고리

    public void updateStock(Integer stock) {
        this.stock = stock;
    }
}
