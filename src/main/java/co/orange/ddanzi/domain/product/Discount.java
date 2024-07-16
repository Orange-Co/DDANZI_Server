package co.orange.ddanzi.domain.product;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.global.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Table(name = "discounts")
@Entity
public class Discount extends BaseTimeEntity {
    @Id
    @Column(name = "product_id")
    private String id;      //상품 고유 ID (PK/FK)

    @MapsId
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "discount_rate", nullable = false)
    private Float discountRate;

    @Column(name = "discount_price", nullable = false)
    private Integer discountPrice;

    @Builder
    public Discount(Product product, Float discountRate, Integer discountPrice) {
        this.product = product;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
    }
}
