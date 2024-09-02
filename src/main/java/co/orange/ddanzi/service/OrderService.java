package co.orange.ddanzi.service;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.exception.*;
import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.order.enums.OrderStatus;
import co.orange.ddanzi.domain.order.enums.PayStatus;
import co.orange.ddanzi.domain.product.Discount;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.OptionDetail;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.mypage.MyOrder;
import co.orange.ddanzi.dto.order.*;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final DiscountRepository discountRepository;


    @Autowired
    AddressService addressService;
    @Autowired
    TermService termService;
    @Autowired
    @Lazy
    PaymentService paymentService;
    @Autowired
    OrderOptionDetailService orderOptionDetailService;


    @Transactional
    public ApiResponse<?> checkOrderProduct(String productId){

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException());
        Discount discount = discountRepository.findById(productId).orElse(null);

        User user = authUtils.getUser();
        Integer salePrice = product.getOriginPrice() - discount.getDiscountPrice();
        Integer charge = paymentService.calculateCharge(salePrice);

        CheckProductResponseDto responseDto = CheckProductResponseDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .modifiedProductName(createModifiedProductName(product.getName()))
                .imgUrl(product.getImgUrl())
                .originPrice(product.getOriginPrice())
                .addressInfo(addressService.setAddressInfo(user))
                .discountPrice(discount.getDiscountPrice())
                .charge(charge)
                .totalPrice(salePrice+charge)
                .build();
        return ApiResponse.onSuccess(Success.GET_ORDER_PRODUCT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> saveOrder(SaveOrderRequestDto requestDto){

        Order order = orderRepository.findById(requestDto.getOrderId()).orElseThrow(OrderNotFoundException::new);
        log.info("Checking the payment is done.");
        Payment payment = order.getPayment();
        if(!payment.getPayStatus().equals(PayStatus.PAID))
            return ApiResponse.onFailure(Error.PAYMENT_REQUIRED,null);

        User user = authUtils.getUser();
        if(!order.getBuyer().equals(user))
            return ApiResponse.onFailure(Error.PAYMENT_CANNOT_CHANGE,null);

        order.updateStatus(OrderStatus.ORDER_PLACE);
        termService.createOrderAgreements(order);

        createOrderOptionDetails(order, requestDto.getSelectedOptionDetailIdList());
        log.info("Created order option details.");

        return ApiResponse.onSuccess(Success.CREATE_ORDER_SUCCESS, SaveOrderResponseDto.builder().orderId(order.getId()).orderStatus(order.getStatus()).build());
    }

    @Transactional
    public ApiResponse<?> getOrder(String orderId){
        User user = authUtils.getUser();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());
        Item item = order.getItem();
        Payment payment = order.getPayment();
        return ApiResponse.onSuccess(Success.GET_ORDER_DETAIL_SUCCESS, setOrderResponseDto(user, order, item, payment));
    }

    @Transactional
    public ApiResponse<?> confirmedOrderToBuy(String orderId){
        User user = authUtils.getUser();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());

        if(!order.getBuyer().equals(user) || order.getStatus()!=OrderStatus.SHIPPING)
            return ApiResponse.onFailure(Error.UNAUTHORIZED_USER,null);

        order.updateStatus(OrderStatus.COMPLETED);

        return ApiResponse.onSuccess(Success.GET_ORDER_DETAIL_SUCCESS, UpdateOrderResponseDto.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus())
                .build());
    }

    @Transactional
    public ApiResponse<?> confirmedOrderToSale(String orderId){
        User user = authUtils.getUser();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());

        if(!order.getItem().getSeller().equals(user) || order.getStatus()!=OrderStatus.ORDER_PLACE)
            return ApiResponse.onFailure(Error.UNAUTHORIZED_USER,null);

        order.updateStatus(OrderStatus.SHIPPING);

        return ApiResponse.onSuccess(Success.GET_ORDER_DETAIL_SUCCESS, UpdateOrderResponseDto.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus())
                .build());
    }

    public Order createOrderRecord(User buyer, Item item){
        String orderId = createOrderId(item.getId());
        Order newOrder = Order.builder()
                    .id(orderId)
                    .buyer(buyer)
                    .item(item)
                    .createdAt(LocalDateTime.now())
                    .status(OrderStatus.ORDER_PENDING)
                    .build();
        log.info("Created new order.");
        return orderRepository.save(newOrder);
    }

    public Order getOrderRecord(String orderId){
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());
    }

    private String createModifiedProductName(String productName){
        return productName.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9,._\\s ]", "");
    }

    private void createOrderOptionDetails(Order order, List<Long> optionDetailIds){
        for(Long optionDetailId : optionDetailIds){
            OptionDetail optionDetail = orderOptionDetailService.getOptionDetailById(optionDetailId);
            orderOptionDetailService.createOrderOptionDetail(order, optionDetail);
        }
    }


    private String createOrderId(String itemId){
        String uploadDatePart = itemId.substring(itemId.length() - 8, itemId.length() - 2);
        LocalDate uploadDate = LocalDate.parse(uploadDatePart, DateTimeFormatter.ofPattern("yyMMdd"));

        LocalDate currentDate = LocalDate.now();
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(uploadDate, currentDate);

        Random random = new Random();
        char firstChar = (char) ('A' + random.nextInt(26));
        char secondChar = (char) ('A' + random.nextInt(26));
        char thirdChar = (char) ('A' + random.nextInt(26));
        return itemId + daysBetween + firstChar + secondChar + thirdChar;
    }

    private OrderResponseDto setOrderResponseDto(User user, Order order, Item item, Payment payment){
        Product product = item.getProduct();
        Discount discount = discountRepository.findById(product.getId()).orElseThrow(() -> new DiscountNotFoundException());

        OrderResponseDto responseDto = OrderResponseDto.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus())
                .productName(product.getName())
                .imgUrl(product.getImgUrl())
                .originPrice(product.getOriginPrice())
                .addressInfo(addressService.setAddressInfo(user))
                .sellerNickname(item.getSeller().getNickname())
                .paymentMethod(payment.getMethod().getDescription())
                .paidAt(payment.getEndedAt())
                .discountPrice(discount.getDiscountPrice())
                .charge(payment.getCharge())
                .totalPrice(payment.getTotalPrice())
                .build();

        return responseDto;
    }

    public Integer getMyOrderCount(User user){
        return orderRepository.countAllByBuyer(user);
    }

    public List<MyOrder> getMyOrderList(User user){
        List<Order> orderList = orderRepository.findByBuyer(user);

        List<MyOrder> orderProductList = new ArrayList<>();

        for (Order order : orderList) {
            Product product = order.getItem().getProduct();
            Discount discount = discountRepository.findById(product.getId()).orElse(null);
            Payment payment = order.getPayment();
            MyOrder myOrder = MyOrder.builder()
                    .productId(product.getId())
                    .orderId(order.getId())
                    .productName(product.getName())
                    .imgUrl(product.getImgUrl())
                    .originPrice(product.getOriginPrice())
                    .salePrice(product.getOriginPrice()-discount.getDiscountPrice())
                    .paidAt(payment.getEndedAt())
                    .build();

            orderProductList.add(myOrder);
        }
        return orderProductList;
    }

}
