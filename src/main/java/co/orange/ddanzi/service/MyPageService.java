package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.ProductInfo;
import co.orange.ddanzi.dto.mypage.MyPageInterestResponseDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.InterestProductRepository;
import co.orange.ddanzi.repository.ProductRepository;
import co.orange.ddanzi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {
    private final UserRepository userRepository;
    private final InterestProductRepository interestProductRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ApiResponse<?> getMyPage(){
        User user = userRepository.findById(1L).orElse(null);
        String nickname = user.getNickname();
        Map<String, Object> response = new HashMap<>();
        response.put("nickname", nickname);
        return ApiResponse.onSuccess(Success.GET_MY_PAGE_INFO_SUCCESS, response);
    }

    @Transactional
    public ApiResponse<?> getInterest(){
        User user = userRepository.findById(1L).orElse(null);
        log.info("찜한 상품 찾기");
        List<Product> productList = interestProductRepository.findProductsByUserId(user.getId());
        log.info("찜한 상품의 정보 입력하기");
        List<ProductInfo> productInfoList = HomeService.setProductList(productList,interestProductRepository);
        MyPageInterestResponseDto responseDto = MyPageInterestResponseDto.builder()
                .totalCount(productList.size())
                .productList(productInfoList)
                .build();
        return ApiResponse.onSuccess(Success.GET_MY_INTEREST_LIST_SUCCESS, responseDto);
    }
}
