package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.dto.home.ProductInfo;
import co.orange.ddanzi.dto.search.SearchResultResponseDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {
    private final ProductRepository productRepository;

    @Transactional
    public ApiResponse<?> searchKeyword(String keyword) {
        List<Product> productList = productRepository.findAllByName(keyword);
        List<ProductInfo> productInfoList = HomeService.getProductList(productList);
        return ApiResponse.onSuccess(Success.GET_SEARCH_RESULTS_SUCCESS, SearchResultResponseDto.builder()
                .searchedProductList(productInfoList).build());
    }
}
