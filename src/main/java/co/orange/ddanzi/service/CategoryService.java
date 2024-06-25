package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Category;
import co.orange.ddanzi.domain.product.Discount;
import co.orange.ddanzi.repository.CategoryRepository;
import co.orange.ddanzi.repository.DiscountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final DiscountRepository discountRepository;

    @Transactional
    public Pair<Category, Float> createOrGetCategory(String fullPath, Boolean isForbidden){
        log.info("카테고리 생성 시작");
        String[] categories = fullPath.split(">");
        Category parentCategory = null;
        Float discountRate = 0.3f;

        for (String categoryContent : categories) {
            log.info("카테고리 생성 -> {}", categoryContent);
            if (parentCategory == null) {
                // 루트 카테고리의 경우
                parentCategory = getRootCategoryOrCreate(categoryContent, null, isForbidden);
                discountRate = parentCategory.getDiscount().getRate();
            }
            else {
                parentCategory = getCategoryOrCreate(categoryContent, parentCategory, isForbidden);
            }
        }
        log.info("카테고리 생성 종료");
        return Pair.of(parentCategory, discountRate);
    }

    public Category getRootCategoryOrCreate(String content, Category parentCategory, Boolean isForbidden) {
        return categoryRepository.findByContentAndParentCategory(content, parentCategory)
                .orElseGet(() -> {
                    Discount newDiscount = createDiscount();
                    log.info("root category 객체 생성");
                    Category newCategory = Category.builder()
                            .content(content)
                            .isForbidden(isForbidden)
                            .parentCategory(parentCategory)
                            .discount(newDiscount)
                            .build();
                    return categoryRepository.save(newCategory);
                });
    }

    public Discount createDiscount(){
        log.info("discount 객체 생성");
        Discount newDiscount = Discount.builder()
                .rate(0.3f)
                .build();
        return  discountRepository.save(newDiscount);
    }

    public Category getCategoryOrCreate(String content, Category parentCategory, Boolean isForbidden) {
        return categoryRepository.findByContentAndParentCategory(content, parentCategory)
                .orElseGet(() -> {
                    Category newCategory = Category.builder()
                            .content(content)
                            .isForbidden(isForbidden)
                            .parentCategory(parentCategory)
                            .build();
                    return categoryRepository.save(newCategory);
                });
    }
}
