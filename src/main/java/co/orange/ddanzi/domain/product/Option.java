package co.orange.ddanzi.domain.product;

import co.orange.ddanzi.domain.product.enums.OptionType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;            //옵션 고유 ID

    @Column(name = "type")
    private OptionType type;    //옵션 종류

    @Column(name = "content")
    private String content;     //옵션 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;    //참조하는 상품

    @Builder
    public Option(final OptionType type, final Product product, final String content) {
        this.type = type;
        this.product = product;
        this.content = content;
    }
}
