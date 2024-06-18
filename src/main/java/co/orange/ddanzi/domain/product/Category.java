package co.orange.ddanzi.domain.product;

import co.orange.ddanzi.global.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "categories")
@Entity
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;                           //카테고리 고유 ID

    @Column(name = "content")
    private String content;                     //카테고리 이름

    @ColumnDefault("false")
    @Column(name = "is_forbidden", nullable = false)
    private Boolean isForbidden;                //금지 여부

    @ColumnDefault("0.3")
    @Column(name = "discount_rate")
    private Float discountRate;                 //할인율

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> childrenCategory = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id", nullable = true)
    private Category parentCategory;

    @Builder
    public Category(Long id, String content, Boolean isForbidden, Float discountRate) {
        this.id = id;
        this.content = content;
        this.isForbidden = isForbidden;
        this.discountRate = discountRate;
    }
}
