package co.orange.ddanzi.domain.product;

import co.orange.ddanzi.domain.product.enums.OptionType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Table(name = "options")
@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;            //옵션 고유 ID

    @ColumnDefault("'옵션'")
    @Column(name = "content", nullable = false)
    private String content;        //옵션 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;    //참조하는 상품

    @Builder
    public Option(String content, Product product) {
        this.content = content;
        this.product = product;
    }
}
