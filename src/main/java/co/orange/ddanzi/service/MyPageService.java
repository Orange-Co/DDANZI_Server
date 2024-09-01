package co.orange.ddanzi.service;

import co.orange.ddanzi.common.exception.PaymentNotFoundException;
import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.product.Discount;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.common.ProductInfo;
import co.orange.ddanzi.dto.mypage.*;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.DiscountRepository;
import co.orange.ddanzi.repository.InterestProductRepository;
import co.orange.ddanzi.repository.OrderRepository;
import co.orange.ddanzi.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {
    private final AuthUtils authUtils;
    private final HomeService homeService;
    private final InterestProductRepository interestProductRepository;

    @Autowired
    OrderService orderService;
    @Autowired
    ItemService itemService;

    @Transactional
    public ApiResponse<?> getMyPage(){
        User user = authUtils.getUser();
        String nickname = user.getNickname();
        Map<String, Object> response = new HashMap<>();
        response.put("nickname", nickname);
        return ApiResponse.onSuccess(Success.GET_MY_PAGE_INFO_SUCCESS, response);
    }

    @Transactional
    public ApiResponse<?> getMyOrder(){
        User user = authUtils.getUser();
        Integer totalCount = orderService.getMyOrderCount(user);
        List<MyOrder> orderProductList = orderService.getMyOrderList(user);

        return ApiResponse.onSuccess(Success.GET_MY_ORDER_LIST_SUCCESS, MyOrderResponseDto.builder()
                .totalCount(totalCount)
                .orderProductList(orderProductList)
                .build());
    }

    @Transactional
    public ApiResponse<?> getMyItem(){
        User user = authUtils.getUser();
        Integer totalCount = itemService.getMyItemCount(user);
        List<MyItem> myItemList = itemService.getMyItemList(user);

        MyItemResponseDto responseDto = MyItemResponseDto.builder()
                .totalCount(totalCount)
                .itemProductList(myItemList)
                .build();
        return ApiResponse.onSuccess(Success.GET_MY_ITEM_LIST_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> getInterest(){
        User user = authUtils.getUser();
        log.info("찜한 상품 찾기");
        List<Product> productList = interestProductRepository.findProductsByUserId(user.getId());
        log.info("찜한 상품의 정보 입력하기");
        List<ProductInfo> productInfoList = homeService.setProductList(user, productList,interestProductRepository);
        MyPageInterestResponseDto responseDto = MyPageInterestResponseDto.builder()
                .totalCount(productList.size())
                .productList(productInfoList)
                .build();
        return ApiResponse.onSuccess(Success.GET_MY_INTEREST_LIST_SUCCESS, responseDto);
    }
}
