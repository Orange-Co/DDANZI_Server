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
    private Long id;

    @ColumnDefault("0.3")
    @Column(name = "rate", nullable = false)
    private Float rate;

    @OneToOne(mappedBy = "discount")
    private Category category;

    @Builder
    public Discount(Long id, Float rate, Category category) {
        this.id = id;
        this.rate = rate;
        this.category = category;
    }
}
