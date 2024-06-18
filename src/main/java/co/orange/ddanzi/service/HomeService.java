package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.dto.home.HomeResponseDto;
import co.orange.ddanzi.dto.home.ProductInfo;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeService {
    private final ProductRepository productRepository;

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
}
