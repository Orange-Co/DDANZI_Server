package co.orange.ddanzi.domain.product;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Table(name = "option_details")
@Entity
public class OptionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_detail_id")
    private Long id;            //세뷰 옵션 고유 ID

    @Column(name = "content")
    private String content;     //세부 옵션 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;    //참조하는 옵션

    @Builder
    public OptionDetail(String content, Option option) {
        this.content = content;
        this.option = option;
    }
}
