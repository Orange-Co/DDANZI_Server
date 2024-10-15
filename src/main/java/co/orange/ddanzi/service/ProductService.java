package co.orange.ddanzi.service;

import co.orange.ddanzi.common.exception.DiscountNotFoundException;
import co.orange.ddanzi.common.exception.ProductNotFoundException;
import co.orange.ddanzi.domain.product.Discount;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.Account;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.dto.item.CheckItemResponseDto;
import co.orange.ddanzi.dto.product.ProductItemResponseDto;
import co.orange.ddanzi.dto.product.ProductRequestDto;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final AccountRepository accountRepository;

    @Autowired
    RestTemplate restTemplate;

    @Transactional
    public ApiResponse<?> getMostSimilarProduct(@RequestBody ProductRequestDto requestDto){
        String productId = getMostSimilarProductId(requestDto);
        if(productId == null) {
            return ApiResponse.onSuccess(Success.GET_MOST_SIMILAR_PRODUCT_SUCCESS, CheckItemResponseDto.builder()
                    .productId("")
                    .productName("")
                    .imgUrl("")
                    .build());
        }
        log.info("Find product by id: {}", productId);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        CheckItemResponseDto responseDto = CheckItemResponseDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .imgUrl(product.getImgUrl())
                .build();
        return ApiResponse.onSuccess(Success.GET_MOST_SIMILAR_PRODUCT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> getProductForItem(String productId){
        User user = authUtils.getUser();

        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        Discount discount = discountRepository.findById(productId).orElseThrow(DiscountNotFoundException::new);
        Account account = accountRepository.findByUserId(user);

        ProductItemResponseDto responseDto = ProductItemResponseDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .imgUrl(product.getImgUrl())
                .originPrice(product.getOriginPrice())
                .salePrice(product.getOriginPrice() - discount.getDiscountPrice())
                .isAccountExist(account != null)
                .build();
        return ApiResponse.onSuccess(Success.GET_ITEM_PRODUCT_SUCCESS, responseDto);
    }

    public String getMostSimilarProductId(ProductRequestDto requestDto){
        log.info("Start to get more similar product from AI Server");
        String path = "/api/v1/image";
        try {
            Map<String, String> result = restTemplate.postForObject(path, requestDto, Map.class);
            return result.get("productId");
        } catch (HttpServerErrorException e) {
            log.error("Server error occurred while fetching similar product", e);
            return null;
        }
    }


    /*
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
        Discount discount = discountRepository.findById(product.getId()).orElse(null);
        ConfirmProductResponseDto responseDto = ConfirmProductResponseDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .originPrice(product.getOriginPrice())
                .salePrice(product.getOriginPrice() - discount.getDiscountPrice())
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

    */
}
