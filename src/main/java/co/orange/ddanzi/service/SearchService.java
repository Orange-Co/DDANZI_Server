package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.common.ProductInfo;
import co.orange.ddanzi.dto.search.SearchPageResponseDto;
import co.orange.ddanzi.dto.search.SearchResultResponseDto;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.global.redis.RedisRepository;
import co.orange.ddanzi.repository.InterestProductRepository;
import co.orange.ddanzi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {
    private final AuthUtils authUtils;
    private final HomeService homeService;
    private final ProductRepository productRepository;
    private final InterestProductRepository interestProductRepository;
    private final RedisRepository redisRepository;

    @Transactional
    public ApiResponse<?> searchPage(String devicetoken) {
        User user = authUtils.getUser();
        List<String> topSearchedList = List.of("멀티비타민", "망고", "핸드크림");
        log.info("Searching page for devicetoken: {}", devicetoken);
        List<String> recentViewedProductIds = redisRepository.getRecentProducts(devicetoken);
        List<Product> productList = productRepository.findByIdIn(recentViewedProductIds);
        List<ProductInfo> productInfoList = homeService.setProductList(user, productList, interestProductRepository);
        return ApiResponse.onSuccess(Success.GET_SEARCH_SCREEN_SUCCESS, SearchPageResponseDto.builder()
                .topSearchedList(topSearchedList)
                .recentlyViewedList(productInfoList)
                .build());
    }

    @Transactional
    public ApiResponse<?> searchKeyword(String keyword) {
        User user = authUtils.getUser();
        List<String> topSearchedList = List.of("예시1", "예시2", "예시3");
        log.info("Search for keyword: {}", keyword);
        List<Product> productList = productRepository.findAllByName(keyword);
        List<ProductInfo> productInfoList = homeService.setProductList(user, productList, interestProductRepository);
        return ApiResponse.onSuccess(Success.GET_SEARCH_RESULTS_SUCCESS, SearchResultResponseDto.builder()
                        .topSearchedList(topSearchedList)
                        .searchedProductList(productInfoList)
                        .build());
    }
}
