package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Category;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.item.ConfirmProductRequestDto;
import co.orange.ddanzi.dto.item.ConfirmProductResponseDto;
import co.orange.ddanzi.dto.item.SaveItemRequestDto;
import co.orange.ddanzi.dto.item.SaveItemResponseDto;
import co.orange.ddanzi.global.common.exception.Error;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.*;
import jakarta.transaction.Transactional;
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

    @Transactional
    public ApiResponse<?> confirmProduct(ConfirmProductRequestDto requestDto){
        Product product = productRepository.findByKakaoProductId(requestDto.getKakaoProductId());
        if(product == null){
            Pair<Category, Float> leafCategoryAndDiscountRate = categoryService.createOrGetCategory(requestDto.getCategory(), requestDto.getIsForbidden());
            log.info("leaf category 찾기 성공 category_id -> {}",leafCategoryAndDiscountRate.getFirst().getId());

            Integer discountPrice = (int)(requestDto.getOriginPrice()* leafCategoryAndDiscountRate.getSecond());
            log.info("할인가 계산 완료 -> {}",discountPrice);

            Product newProduct = requestDto.toProduct(discountPrice, leafCategoryAndDiscountRate.getFirst());
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

    @Transactional
    public ApiResponse<?> saveItem(User user, SaveItemRequestDto requestDto){
        Product product = productRepository.findById(requestDto.getProductId()).orElse(null);
        if(product == null)
            return ApiResponse.onFailure(Error.PRODUCT_NOT_FOUND, null);

        Item newItem = requestDto.toItem(user, product);
        newItem = itemRepository.save(newItem);
        log.info("item 등록 성공, item_id: {}",newItem.getId());

        product.updateStock(product.getStock() + 1);
        log.info("상품의 재고 수량 업데이트 -> {}개",  product.getStock());

        SaveItemResponseDto responseDto = SaveItemResponseDto.builder()
                .itemId(newItem.getId())
                .productName(product.getName())
                .originPrice(product.getOriginPrice())
                .build();
        return ApiResponse.onSuccess(Success.CREATE_ITEM_SUCCESS, responseDto);
    }
}
