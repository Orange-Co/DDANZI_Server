package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.Banner;
import co.orange.ddanzi.domain.product.*;
import co.orange.ddanzi.dto.ProductInfo;
import co.orange.ddanzi.dto.home.*;
import co.orange.ddanzi.global.common.exception.Error;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeService {
    private final ProductRepository productRepository;
    private final BannerRepository bannerRepository;
    private final DefaultDiscountRepository defaultDiscountRepository;
    private final OptionRepository optionRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final InterestProductRepository interestProductRepository;

    @Transactional
    public ApiResponse<?> getProductList(){
        Banner banner = bannerRepository.findByIsSelected(Boolean.TRUE);
        List<Product> productList = productRepository.findAllByStock(0);
        List<ProductInfo> productInfoList = setProductList(productList, interestProductRepository);
        HomeResponseDto responseDto = HomeResponseDto.builder()
                .homeImgUrl(banner.getImgUrl())
                .productList(productInfoList).build();
        return ApiResponse.onSuccess(Success.GET_HOME_INFO_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> getProductDetail(String productId){
        log.info("상품 조회 -> product_id: {}", productId);
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null){
            return ApiResponse.onFailure(Error.PRODUCT_NOT_FOUND, null);
        }
        log.info("해당 상품의 리프 카테고리 찾기");
        if(product.getLeafCategory() == null){
            ApiResponse.onFailure(Error.CATEGORY_NOT_FOUND, null);
        }
        Category leafCategory = product.getLeafCategory();
        log.info("카테고리 조회 성공 -> catgory_id: {}", leafCategory.getId());

        log.info("루트 카테고리와 full path 조회");
        Pair<Category, String> rootCategoryAndFullPath = leafCategory.getRootCategoryAndFullPath();
        Category rootCategory = rootCategoryAndFullPath.getFirst();
        String categoryFullPath = rootCategoryAndFullPath.getSecond();

        log.info("루트 카테고리의 할인율 조회 -> catgory_id: {}",rootCategory.getId());
        DefaultDiscount defaultDiscountEntity = defaultDiscountRepository.findByCategoryId(rootCategory.getId());
        if(defaultDiscountEntity == null){
            ApiResponse.onFailure(Error.DISCOUNT_INFO_NOT_FOUND, null);
        }
        Float discountRateFloat = defaultDiscountEntity.getRate() * 100;
        Integer discountRate = discountRateFloat.intValue();

        // 옵션 관련 정보 삭제
        //log.info("해당 상품의 옵션 조회");
        //List<OptionInfo> optionList = getOptionList(productId);

        log.info("해당 상품의 찜 개수 조회");
        Integer interestCount = interestProductRepository.countByProductIdWithLimit(productId);


        HomeDetailResponseDto responseDto = HomeDetailResponseDto.builder()
                .name(product.getName())
                .category(categoryFullPath)
                //.isOptionExist(!optionList.isEmpty())
                .isImminent(true)
                .discountRate(discountRate)
                .stockCount(product.getStock())
                .infoUrl(product.getInfoUrl())
                .interestCount(interestCount)
                //.optionList(optionList)
                .build();

        return ApiResponse.onSuccess(Success.GET_PRODUCT_DETAIL_SUCCESS,responseDto);
    }

    public static List<ProductInfo> setProductList(List<Product> productList, InterestProductRepository interestProductRepository){
        List<ProductInfo> productInfoList = new ArrayList<>();
        for(Product product : productList){
            productInfoList.add(ProductInfo.builder()
                    .productId(product.getId())
                    .kakaoProductId(product.getKakaoProductId())
                    .name(product.getName())
                    .originPrice(product.getOriginPrice())
                    .salePrice(product.getOriginPrice() - product.getDiscountPrice())
                    .imgUrl(product.getImgUrl())
                    .interestCount(interestProductRepository.countByProductIdWithLimit(product.getId()))
                    .build());
        }
        return productInfoList;
    }

    private List<OptionInfo> getOptionList(String productId){
        List<Option> optionList = optionRepository.findAllByProductId(productId);
        List<OptionInfo> optionInfoList = new ArrayList<>();
        for(Option option : optionList){
            log.info("세부 옵션 조회  -> option_id: {}", option.getId());
            List<OptionDetail> optionDetailList = optionDetailRepository.findAllByOptionId(option.getId());
            List<OptionDetailInfo> optionDetailInfoList = new ArrayList<>();
            for(OptionDetail optionDetail : optionDetailList){
                optionDetailInfoList.add(OptionDetailInfo.builder()
                                .optionDetailId(optionDetail.getId())
                                .content(optionDetail.getContent())
                                //.isAvailable(optionDetail.getIsAvailable())
                                .build());
            }
            log.info("세부 옵션 조회 성공 -> option_id: {}", option.getId());
            optionInfoList.add(OptionInfo.builder()
                            .optionId(option.getId())
                            .type(option.getType())
                            .optionDetailList(optionDetailInfoList)
                    .build());
        }
        log.info("해당 상품의 전체 옵션 조회 성공 -> product_id: {}",productId);
        return optionInfoList;
    }
}
