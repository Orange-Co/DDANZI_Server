package co.orange.ddanzi.domain.product;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long id;                //할인율 고유 ID

    @ColumnDefault("0.3")
    @Column(name = "rate", nullable = false)
    private Float rate;             //할인율, 디폴트 30%

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_category_id", nullable = true)
    private Category rootCategory;      //카테고리 = root category

    @Builder
    public Discount(Long id, Float rate, Category rootCategory) {
        this.id = id;
        this.rate = rate;
        this.rootCategory = rootCategory;
    }
}
