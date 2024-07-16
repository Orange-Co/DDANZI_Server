package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.dto.ProductInfo;
import co.orange.ddanzi.dto.search.SearchResultResponseDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.InterestProductRepository;
import co.orange.ddanzi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {
    private final HomeService homeService;
    private final ProductRepository productRepository;
    private final InterestProductRepository interestProductRepository;

    @Transactional
    public ApiResponse<?> searchKeyword(String keyword) {
        List<String> topSearchedList = List.of("예시1", "예시2", "예시3");
        List<Product> productList = productRepository.findAllByName(keyword);
        List<ProductInfo> productInfoList = homeService.setProductList(productList, interestProductRepository);
        return ApiResponse.onSuccess(Success.GET_SEARCH_RESULTS_SUCCESS, SearchResultResponseDto.builder()
                        .topSearchedList(topSearchedList)
                        .searchedProductList(productInfoList)
                        .build());
    }
}
