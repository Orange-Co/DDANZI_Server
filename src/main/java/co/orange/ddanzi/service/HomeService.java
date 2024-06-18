package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Category;
import co.orange.ddanzi.domain.product.Discount;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.dto.home.HomeDetailResponseDto;
import co.orange.ddanzi.dto.home.HomeResponseDto;
import co.orange.ddanzi.dto.home.ProductInfo;
import co.orange.ddanzi.global.common.exception.Error;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final DiscountRepository discountRepository;
    private final OptionRepository optionRepository;
    private final OptionDetailRepository optionDetailRepository;

    @Transactional
    public ApiResponse<?> getProductList(){
        List<Product> productList = productRepository.findAllByStock(0);
        List<ProductInfo> productInfoList = new ArrayList<>();
        for(Product product : productList){
            productInfoList.add(ProductInfo.builder()
                            .productId(product.getId())
                            .name(product.getName())
                            .originPrice(product.getOriginPrice())
                            .salePrice(product.getOriginPrice() - product.getDiscountPrice())
                            .interestCount(product.getInterestCount())
                    .build());
        }
        HomeResponseDto responseDto = HomeResponseDto.builder().productList(productInfoList).build();
        return ApiResponse.onSuccess(Success.GET_HOME_INFO_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> getProductDetail(Long productId){
        log.info("상품 고유 ID가 {} 인 상품 찾기", productId);
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null){
            return ApiResponse.onFailure(Error.PRODUCT_NOT_FOUND, null);
        }

        log.info("해당 상품의 리프 카테고리 찾기");
        Optional<Category> leafCategory = categoryRepository.findLeafCategoryByProductId(productId);
        if(leafCategory.isEmpty()){
            ApiResponse.onFailure(Error.CATEGORY_NOT_FOUND, null);
        }
        log.info("카테고리 조회 성공");
        String categoryFullPath = leafCategory.get().getFullPath();
        Float discountRateFloat = discountRepository.findByCategoryId(leafCategory.get().getId()).getRate() * 100;
        Integer discountRate = discountRateFloat.intValue();


        HomeDetailResponseDto responseDto = HomeDetailResponseDto.builder()
                .name(product.getName())
                .category(categoryFullPath)
                .isOptionExist()
                .isImminent()
                .discountRate(discountRate)
                .stockCount(product.getStock())
                .infoUrl(product.getInfoUrl())
                .interestCount(product.getInterestCount())
                .optionList()
                .build();

        return ApiResponse.onSuccess(Success.GET_PRODUCT_DETAIL_SUCCESS,);
    }
}
