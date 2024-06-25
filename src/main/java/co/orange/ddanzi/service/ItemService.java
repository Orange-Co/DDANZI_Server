package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Category;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.dto.item.ConfirmProductRequestDto;
import co.orange.ddanzi.dto.item.ConfirmProductResponseDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.CategoryRepository;
import co.orange.ddanzi.repository.ItemRepository;
import co.orange.ddanzi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemService {
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final CategoryService categoryService;

    public ApiResponse<?> confirmProduct(ConfirmProductRequestDto requestDto){
        Product product = productRepository.findByKakaoProductId(requestDto.getKakaoProductId());
        if(product == null){
            Pair<Category, Float> leafCategoryAndDiscountRate = categoryService.createOrGetCategory(requestDto.getCategory(), requestDto.getIsForbidden());

            Integer discountPrice = (int)(requestDto.getOriginPrice()* leafCategoryAndDiscountRate.getSecond());
            log.info("할인가 계산 완료 -> {}",discountPrice);

            Product newProduct = requestDto.toProduct(discountPrice, leafCategoryAndDiscountRate.getFirst());
            productRepository.save(newProduct);
            log.info("상품 등록 완료 -> product_id: {}",product.getId());
            product = newProduct;
        }
        ConfirmProductResponseDto responseDto = ConfirmProductResponseDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .originPrice(product.getOriginPrice())
                .salePrice(product.getOriginPrice() - product.getDiscountPrice())
                .build();
        return ApiResponse.onSuccess(Success.CREATE_PRODUCT_SUCCESS, responseDto);
    }
}
