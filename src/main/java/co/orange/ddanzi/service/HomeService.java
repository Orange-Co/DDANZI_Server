package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.Banner;
import co.orange.ddanzi.domain.product.*;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.ProductInfo;
import co.orange.ddanzi.dto.home.*;
import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.global.redis.RedisRepository;
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
    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final BannerRepository bannerRepository;
    private final DiscountRepository discountRepository;
    private final OptionRepository optionRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final InterestProductRepository interestProductRepository;
    private final RedisRepository redisRepository;

    @Transactional
    public ApiResponse<?> getProductList(){
        User user = authUtils.getUser();
        Banner banner = bannerRepository.findByIsSelected(Boolean.TRUE);
        List<Product> productList = productRepository.findAllByStock(0);

        List<ProductInfo> productInfoList = new ArrayList<>();
        if(user!=null) {
            log.info("User is not null");
            productInfoList = setProductList(user, productList, interestProductRepository);
        }
        else{
            log.info("User is null");
            productInfoList = setProductListInNotUser(productList, interestProductRepository);
        }


        HomeResponseDto responseDto = HomeResponseDto.builder()
                .homeImgUrl(banner.getImgUrl())
                .productList(productInfoList).build();
        return ApiResponse.onSuccess(Success.GET_HOME_INFO_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> getProductDetail(String devicetoken, String productId){
        User user = authUtils.getUser();

        log.info("상품 조회 -> product_id: {}", productId);
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null){
            return ApiResponse.onFailure(Error.PRODUCT_NOT_FOUND, null);
        }

        Boolean isInterested = Boolean.FALSE;
        if(user!=null) {
            log.info("User is not null");
            isInterested = interestProductRepository.existsByIdUserAndIdProduct(user, product);
        }
        log.info("해당 상품의 리프 카테고리 찾기");
        if(product.getLeafCategory() == null){
            ApiResponse.onFailure(Error.CATEGORY_NOT_FOUND, null);
        }
        Category leafCategory = product.getLeafCategory();

        log.info("카테고리 full path 조회");
        String categoryFullPath = leafCategory.getFullPath();

        log.info("상품 할인율 조회");
        Discount discount = discountRepository.findById(productId).orElse(null);
        if(discount == null){
            ApiResponse.onFailure(Error.DISCOUNT_INFO_NOT_FOUND, null);
        }
        Float discountRateFloat = discount.getDiscountRate() * 100;
        Integer discountRate = discountRateFloat.intValue();

        log.info("해당 상품의 옵션 조회");
        List<OptionInfo> optionList = getOptionList(productId);

        log.info("해당 상품의 찜 개수 조회");
        Integer interestCount = interestProductRepository.countByProductIdWithLimit(productId);

        redisRepository.saveDeviceToken(devicetoken, productId);
        log.info("최근 본 상품 등록");

        HomeDetailResponseDto responseDto = HomeDetailResponseDto.builder()
                .name(product.getName())
                .imgUrl(product.getImgUrl())
                .category(categoryFullPath)
                .isOptionExist(!optionList.isEmpty())
                .isImminent(true)
                .discountRate(discountRate)
                .originPrice(product.getOriginPrice())
                .salePrice(product.getOriginPrice() - discount.getDiscountPrice())
                .infoUrl(product.getInfoUrl())
                .stockCount(product.getStock())
                .infoUrl(product.getInfoUrl())
                .isInterested(isInterested)
                .interestCount(interestCount)
                .optionList(optionList)
                .build();

        return ApiResponse.onSuccess(Success.GET_PRODUCT_DETAIL_SUCCESS,responseDto);
    }

    public List<ProductInfo> setProductList(User user, List<Product> productList, InterestProductRepository interestProductRepository){
        List<ProductInfo> productInfoList = new ArrayList<>();
        for(Product product : productList){
            Discount discount = discountRepository.findById(product.getId()).orElse(null);
            productInfoList.add(ProductInfo.builder()
                    .productId(product.getId())
                    .kakaoProductId(product.getKakaoProductId())
                    .name(product.getName())
                    .originPrice(product.getOriginPrice())
                    .salePrice(product.getOriginPrice() - discount.getDiscountPrice())
                    .imgUrl(product.getImgUrl())
                    .interestCount(interestProductRepository.countByProductIdWithLimit(product.getId()))
                    .isInterested(interestProductRepository.existsByIdUserAndIdProduct(user, product))
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
                                .isAvailable(optionDetail.getIsAvailable())
                                .build());
            }
            log.info("세부 옵션 조회 성공 -> option_id: {}", option.getId());
            optionInfoList.add(OptionInfo.builder()
                            .optionId(option.getId())
                            .type(option.getContent())
                            .optionDetailList(optionDetailInfoList)
                    .build());
        }
        log.info("해당 상품의 전체 옵션 조회 성공 -> product_id: {}",productId);
        return optionInfoList;
    }


    public List<ProductInfo> setProductListInNotUser(List<Product> productList, InterestProductRepository interestProductRepository){
        List<ProductInfo> productInfoList = new ArrayList<>();
        for(Product product : productList){
            Discount discount = discountRepository.findById(product.getId()).orElse(null);
            productInfoList.add(ProductInfo.builder()
                    .productId(product.getId())
                    .kakaoProductId(product.getKakaoProductId())
                    .name(product.getName())
                    .originPrice(product.getOriginPrice())
                    .salePrice(product.getOriginPrice() - discount.getDiscountPrice())
                    .imgUrl(product.getImgUrl())
                    .interestCount(interestProductRepository.countByProductIdWithLimit(product.getId()))
                    .isInterested(false)
                    .build());
        }
        return productInfoList;
    }


    @Transactional
    public ApiResponse<?> getProductDetail_oldVersion(String productId){
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
        //DefaultDiscount defaultDiscountEntity = defaultDiscountRepository.findByCategoryId(rootCategory.getId());
//        if(defaultDiscountEntity == null){
//            ApiResponse.onFailure(Error.DISCOUNT_INFO_NOT_FOUND, null);
//        }
//        Float discountRateFloat = defaultDiscountEntity.getRate() * 100;
//        Integer discountRate = discountRateFloat.intValue();

        log.info("해당 상품의 옵션 조회");
        List<OptionInfo> optionList = getOptionList(productId);

        log.info("해당 상품의 찜 개수 조회");
        Integer interestCount = interestProductRepository.countByProductIdWithLimit(productId);


        HomeDetailResponseDto responseDto = HomeDetailResponseDto.builder()
                .name(product.getName())
                .category(categoryFullPath)
                .isOptionExist(!optionList.isEmpty())
                .isImminent(true)
                //.discountRate(discountRate)
                .stockCount(product.getStock())
                .infoUrl(product.getInfoUrl())
                .interestCount(interestCount)
                .optionList(optionList)
                .build();

        return ApiResponse.onSuccess(Success.GET_PRODUCT_DETAIL_SUCCESS,responseDto);
    }

}





