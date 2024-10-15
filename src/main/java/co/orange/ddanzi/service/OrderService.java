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
import co.orange.ddanzi.domain.product.enums.ItemStatus;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.enums.FcmCase;
import co.orange.ddanzi.dto.mypage.MyOrder;
import co.orange.ddanzi.dto.order.*;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.*;
import co.orange.ddanzi.service.common.*;
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
import java.util.Map;
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

    private final AddressService addressService;
    private final TermService termService;
    private final OrderOptionDetailService orderOptionDetailService;
    private final HistoryService historyService;
    private final FcmService fcmService;

    @Autowired
    @Lazy
    PaymentService paymentService;

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
        Payment payment = paymentRepository.findByOrder(order);
        if(!payment.getPayStatus().equals(PayStatus.PAID))
            return ApiResponse.onFailure(Error.PAYMENT_REQUIRED,null);

        User user = authUtils.getUser();
        if(!order.getBuyer().equals(user))
            return ApiResponse.onFailure(Error.PAYMENT_CANNOT_CHANGE,null);

        order.updateStatus(OrderStatus.ORDER_PLACE);
        termService.createOrderAgreements(order);
        historyService.createOrderHistory(order);

        createOrderOptionDetails(order, requestDto.getSelectedOptionDetailIdList());
        log.info("Created order option details.");

        fcmService.sendMessageToUser(order.getItem().getSeller(), FcmCase.A1, order);

        return ApiResponse.onSuccess(Success.CREATE_ORDER_SUCCESS, SaveOrderResponseDto.builder().orderId(order.getId()).orderStatus(order.getStatus()).build());
    }

    @Transactional
    public ApiResponse<?> getOrder(String orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());
        Item item = order.getItem();
        Payment payment = paymentRepository.findByOrder(order);
        return ApiResponse.onSuccess(Success.GET_ORDER_DETAIL_SUCCESS, setOrderResponseDto(order, item, payment));
    }

    @Transactional
    public ApiResponse<?> confirmedOrderToBuy(String orderId){
        User user = authUtils.getUser();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());

        if(!order.getBuyer().equals(user))
            return ApiResponse.onFailure(Error.UNAUTHORIZED_USER,null);

        if(!(order.getStatus()==OrderStatus.SHIPPING || order.getStatus()==OrderStatus.DELAYED_SHIPPING || order.getStatus() ==OrderStatus.WARNING))
            return ApiResponse.onFailure(Error.INVALID_ORDER_STATUS,Map.of("orderStatus", order.getStatus()));

        order.updateStatus(OrderStatus.COMPLETED);
        historyService.createOrderHistory(order);

        fcmService.sendMessageToUser(order.getItem().getSeller(), FcmCase.A3, order);
        return ApiResponse.onSuccess(Success.GET_ORDER_DETAIL_SUCCESS, UpdateOrderResponseDto.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus())
                .build());
    }

    @Transactional
    public ApiResponse<?> confirmedOrderToSale(String orderId){
        User user = authUtils.getUser();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());

        if(!order.getItem().getSeller().equals(user))
            return ApiResponse.onFailure(Error.UNAUTHORIZED_USER,null);

        if(order.getStatus()!=OrderStatus.ORDER_PLACE)
            return ApiResponse.onFailure(Error.INVALID_ORDER_STATUS, Map.of("orderStatus", order.getStatus()));

        order.updateStatus(OrderStatus.SHIPPING);
        historyService.createOrderHistory(order);
        fcmService.sendMessageToUser(order.getBuyer(), FcmCase.B2, order);

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
                    .status(OrderStatus.ORDER_PENDING)
                    .build();
        log.info("Created new order.");
        return orderRepository.save(newOrder);
    }

    public Order getOrderRecord(String orderId){
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());
    }

    @Transactional
    public void checkOrderPlacedOrder(){
        //입금 후 1일(24시간)이 지났는데, 판매확정이 되지 않았을 시 - 거래 취소
        LocalDateTime oneDayLimit = LocalDateTime.now().minusDays(1);
        List<Order> orderPlaceOrders = orderRepository.findOverLimitTimeOrders(OrderStatus.ORDER_PLACE, oneDayLimit);
        for(Order order : orderPlaceOrders){
            if(paymentService.refundPayment(order.getBuyer(), order, "고객이 판매확정을 하지 않아 거래가 취소되어 결제 금액을 환불합니다.")){
                fcmService.sendMessageToAdmins("⚠️ 관리자 알림: 환불 성공", "거래 취소로 인해 환불되었습니다. orderId:" + order.getId());
                order.updateStatus(OrderStatus.CANCELLED);
                fcmService.sendMessageToUser(order.getItem().getSeller(), FcmCase.A2, order);
                fcmService.sendMessageToUser(order.getBuyer(), FcmCase.B1, order);
            }
            else{
                fcmService.sendMessageToAdmins("‼️관리자 알림: 환불 실패", "거래가 취소로 인한 환불에 실패하였습니다. orderId:" + order.getId());
            }
        }
    }

    @Transactional
    public void checkShippingOrder(){
        //판매확정 후 3일 (72시간)이 지났는데, 구매확정이 되지 않았을 시
        LocalDateTime threeDayLimit = LocalDateTime.now().minusDays(3);
        List<Order> shippingOrders = orderRepository.findOverLimitTimeOrders(OrderStatus.SHIPPING, threeDayLimit);
        for(Order order : shippingOrders){
            fcmService.sendMessageToUser(order.getBuyer(), FcmCase.B3, order);
            order.updateStatus(OrderStatus.DELAYED_SHIPPING);
        }
    }

    @Transactional
    public void checkDelayedShippingOrder(){
        //판매확정 후 6일 (144시간)이 지났는데, 구매확정이 되지 않았고, 신고도 하지 않았을 시
        LocalDateTime sixDayLimit = LocalDateTime.now().minusDays(3);
        List<Order> delayedShippingOrders = orderRepository.findOverLimitTimeOrders(OrderStatus.DELAYED_SHIPPING, sixDayLimit);
        for(Order order : delayedShippingOrders){
            fcmService.sendMessageToUser(order.getBuyer(), FcmCase.B4, order);
            order.updateStatus(OrderStatus.WARNING);
        }
    }

    @Transactional
    public void checkWarningOrder(){
        //판매확정 후 7일 (168시간)이 지났는데, 구매확정이 되지 않았고, 신고도 하지 않았을 시
        LocalDateTime sevenDayLimit = LocalDateTime.now().minusDays(1);
        List<Order> delayedShippingOrders = orderRepository.findOverLimitTimeOrders(OrderStatus.WARNING, sevenDayLimit);
        for(Order order : delayedShippingOrders){
            fcmService.sendMessageToUser(order.getItem().getSeller(), FcmCase.A3, order);
            order.updateStatus(OrderStatus.COMPLETED);
        }
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
        String orderId;
        do {
            String uploadDatePart = itemId.substring(itemId.length() - 8, itemId.length() - 2);
            LocalDate uploadDate = LocalDate.parse(uploadDatePart, DateTimeFormatter.ofPattern("yyMMdd"));

            LocalDate currentDate = LocalDate.now();
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(uploadDate, currentDate);

            Random random = new Random();
            char firstChar = (char) ('A' + random.nextInt(26));
            char secondChar = (char) ('A' + random.nextInt(26));
            char thirdChar = (char) ('A' + random.nextInt(26));

            orderId = itemId + daysBetween + firstChar + secondChar + thirdChar;
        }while (orderRepository.existsById(orderId));
        log.info("Created order id: " + orderId);
        return orderId;
    }

    private OrderResponseDto setOrderResponseDto(Order order, Item item, Payment payment){
        Product product = item.getProduct();
        Discount discount = discountRepository.findById(product.getId()).orElseThrow(() -> new DiscountNotFoundException());

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus())
                .productName(product.getName())
                .imgUrl(product.getImgUrl())
                .originPrice(product.getOriginPrice())
                .addressInfo(addressService.setAddressInfo(order.getBuyer()))
                .sellerNickname(item.getSeller().getNickname())
                .paymentMethod(payment.getMethod().getDescription())
                .paidAt(payment.getEndedAt())
                .discountPrice(discount.getDiscountPrice())
                .charge(payment.getCharge())
                .totalPrice(payment.getTotalPrice())
                .build();
    }

    public List<MyOrder> getMyOrderList(User user){
        List<Order> orderList = orderRepository.findByBuyerAndStatus(user);
        List<MyOrder> orderProductList = new ArrayList<>();
        for (Order order : orderList) {
            Product product = order.getItem().getProduct();
            Discount discount = discountRepository.findById(product.getId()).orElse(null);
            Payment payment = paymentRepository.findByOrder(order);
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
