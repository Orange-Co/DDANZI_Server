package co.orange.ddanzi.domain.term;

import co.orange.ddanzi.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "term_items")
@Entity
public class TermItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_item_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "is_essential")
    private Boolean isEssential;

    @Column(name = "note")
    private String note;

}