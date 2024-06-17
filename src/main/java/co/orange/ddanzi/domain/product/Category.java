package co.orange.ddanzi.domain.product;

import co.orange.ddanzi.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @ColumnDefault("false")
    @Column(name = "is_forbidden", nullable = false)
    private Boolean isForbidden;

    @ColumnDefault("0.3")
    @Column(name = "discount_rate")
    private Float discountRate;

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
