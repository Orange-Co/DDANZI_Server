package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Category;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.dto.item.ConfirmProductRequestDto;
import co.orange.ddanzi.dto.item.ConfirmProductResponseDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.CategoryRepository;
import co.orange.ddanzi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    @Transactional
    public ApiResponse<?> confirmProduct(ConfirmProductRequestDto requestDto){
        Product product = productRepository.findByKakaoProductId(requestDto.getKakaoProductId());
        if(product == null){
            Pair<Category, Float> leafCategoryAndDiscountRate = categoryService.createOrGetCategory(requestDto.getCategory(), requestDto.getIsForbidden());
            log.info("leaf category 찾기 성공 category_id -> {}",leafCategoryAndDiscountRate.getFirst().getId());

            Integer discountPrice = (int)(requestDto.getOriginPrice()* leafCategoryAndDiscountRate.getSecond());
            log.info("할인가 계산 완료 -> {}",discountPrice);

            String productId = createProductId(leafCategoryAndDiscountRate.getFirst());
            Product newProduct = requestDto.toProduct(productId, discountPrice, leafCategoryAndDiscountRate.getFirst());
            productRepository.save(newProduct);
            product = newProduct;
            log.info("상품 등록 완료 -> product_id: {}",product.getId());
        }
        ConfirmProductResponseDto responseDto = ConfirmProductResponseDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .originPrice(product.getOriginPrice())
                .salePrice(product.getOriginPrice() - product.getDiscountPrice())
                .build();
        return ApiResponse.onSuccess(Success.CREATE_PRODUCT_SUCCESS, responseDto);
    }

    public String createProductId(Category leafCategory) {
        log.info("{} 카테고리의 max sequenceNumber 찾기",leafCategory.getContent());
        Integer maxSequenceNumber = productRepository.findMaxSequenceNumberByCategory(leafCategory);
        int nextSequenceNumber = (maxSequenceNumber != null ? maxSequenceNumber + 1 : 1);
        log.info("{} 카테고리의 next sequenceNumber -> {}",leafCategory.getContent(),nextSequenceNumber);

        String categoryIdStr = String.format("%03d", leafCategory.getId());
        String sequenceNumberStr = String.format("%04d", nextSequenceNumber);
        String productId = categoryIdStr + sequenceNumberStr;
        log.info("product_id 생성 완료 -> {}",productId);

        return productId;
    }
}
