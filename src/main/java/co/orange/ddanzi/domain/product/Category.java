package co.orange.ddanzi.domain.product;

import co.orange.ddanzi.global.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.util.Pair;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "discount_id", nullable = true)
    private DefaultDiscount defaultDiscount;                  //할인율

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> childrenCategory = new ArrayList<>();    //자식 카테고리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id", nullable = true)
    private Category parentCategory;                                //부모 카테고리

    @Builder
    public Category(Long id, String content, Boolean isForbidden, Category parentCategory, DefaultDiscount defaultDiscount) {
        this.id = id;
        this.content = content;
        this.isForbidden = isForbidden;
        this.parentCategory = parentCategory;
        this.defaultDiscount = defaultDiscount;
    }

    public String getFullPath(){
        if (parentCategory != null) {
            return parentCategory.getFullPath() + ">" + content;
        } else {
            return content;
        }
    }

    // 루트 카테고리와 전체 경로를 반환하는 함수 추가
    public Pair<Category, String> getRootCategoryAndFullPath() {
        Category currentCategory = this;
        StringBuilder fullPath = new StringBuilder(content);

        while (currentCategory.getParentCategory() != null) {
            currentCategory = currentCategory.getParentCategory();
            fullPath.insert(0, currentCategory.getContent() + ">");
        }

        return Pair.of(currentCategory, fullPath.toString());
    }
}
