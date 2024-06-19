package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.InterestProduct;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.pk.InterestProductId;
import co.orange.ddanzi.dto.interest.InterestResponseDto;
import co.orange.ddanzi.global.common.exception.Error;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.InterestProductRepository;
import co.orange.ddanzi.repository.ProductRepository;
import co.orange.ddanzi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class InterestService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InterestProductRepository interestProductRepository;

    @Transactional
    public ApiResponse<?> addInterest(Long productId) {
        log.info("사용자 조회", productId);
        User user = userRepository.findById(1L).orElse(null);

        log.info("상품 조회 -> product_id: {}", productId);
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            ApiResponse.onFailure(Error.PRODUCT_NOT_FOUND, null);
        }

        log.info("찜 상품 등록 시작");
        InterestProduct interestProduct = new InterestProduct();
        InterestProduct newInterestProduct = interestProduct.toEntity(user, product);
        interestProductRepository.save(newInterestProduct);
        log.info("찜 상품 등록 성공 -> interest_id: {}", newInterestProduct.getId());
        InterestResponseDto responseDto = InterestResponseDto.builder()
                .nickname(user.getNickname())
                .productId(productId)
                .build();
        return ApiResponse.onSuccess(Success.CREATE_INTEREST_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> deleteInterest(Long productId) {
        log.info("사용자 조회", productId);
        User user = userRepository.findById(1L).orElse(null);

        log.info("상품 조회 -> product_id: {}", productId);
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            ApiResponse.onFailure(Error.PRODUCT_NOT_FOUND, null);
        }

        log.info("찜 상품 해제 시작");
        interestProductRepository.deleteByUserAndProduct(user,product);
        log.info("찜 상품 해제 성공");

        InterestResponseDto responseDto = InterestResponseDto.builder()
                .nickname(user.getNickname())
                .productId(productId)
                .build();
        return ApiResponse.onSuccess(Success.DELETE_INTEREST_SUCCESS, responseDto);
    }
}
