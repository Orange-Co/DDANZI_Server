package co.orange.ddanzi.service;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.exception.*;
import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.OrderAgreement;
import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.order.enums.OrderStatus;
import co.orange.ddanzi.domain.order.enums.PayStatus;
import co.orange.ddanzi.domain.product.Discount;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.OptionDetail;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.product.enums.ItemStatus;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.order.CheckProductResponseDto;
import co.orange.ddanzi.dto.order.CreateOrderRequestDto;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.dto.order.OrderResponseDto;
import co.orange.ddanzi.dto.order.UpdateOrderResponseDto;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final DiscountRepository discountRepository;
    private final OptionDetailRepository optionDetailRepository;


    @Autowired
    AddressService addressService;

    @Autowired
    TermService termService;


    @Transactional
    public ApiResponse<?> checkOrderProduct(String productId){

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException());
        Item item = itemRepository.findNearestExpiryItem(product).orElseThrow(()-> new ItemNotFoundException());
        Discount discount = discountRepository.findById(productId).orElse(null);

        User user = authUtils.getUser();

        CheckProductResponseDto responseDto = CheckProductResponseDto.builder()
                .itemId(item.getId())
                .productName(product.getName())
                .imgUrl(product.getImgUrl())
                .originPrice(product.getOriginPrice())
                .addressInfo(addressService.setAddressInfo(user))
                .discountPrice(discount.getDiscountPrice())
                .charge(100)
                .totalPrice(product.getOriginPrice() - discount.getDiscountPrice())
                .build();
        return ApiResponse.onSuccess(Success.GET_ORDER_PRODUCT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> createOrder(CreateOrderRequestDto requestDto){
        log.info("Checking the payment is done.");
        Payment payment = paymentRepository.findById(requestDto.getPaymentId()).orElseThrow(() -> new PaymentNotFoundException());
        if(!payment.getPayStatus().equals(PayStatus.PAID))
            return ApiResponse.onFailure(Error.PAYMENT_REQUIRED,null);

        User user = authUtils.getUser();
        if(!payment.getBuyer().equals(user))
            return ApiResponse.onFailure(Error.PAYMENT_CANNOT_CHANGE,null);

        Item item = itemRepository.findById(requestDto.getItemId()).orElseThrow(() -> new ItemNotFoundException());
        item.updateStatus(ItemStatus.CLOSED);
        log.info("Updated item status.");

        OptionDetail optionDetail = new OptionDetail();
        if(requestDto.getSelectedOptionDetailId() != null) {
            log.info("Selected option detail id : " + requestDto.getSelectedOptionDetailId());
            optionDetail = optionDetailRepository.findById(requestDto.getSelectedOptionDetailId()).orElseThrow(null);
        }
        else
            optionDetail = null;
        log.info("Setting selected optionDetail");


        Order order = createOrderRecord(requestDto, user, item, optionDetail);
        termService.createOrderAgreements(order);

        return ApiResponse.onSuccess(Success.CREATE_ORDER_SUCCESS, setOrderResponseDto(user, order, item, payment));
    }

    @Transactional
    public ApiResponse<?> getOrder(String orderId){
        User user = authUtils.getUser();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());
        Item item = order.getItem();
        Payment payment = paymentRepository.findByBuyerAndItem(user, item);
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


    private Order createOrderRecord(CreateOrderRequestDto requestDto, User user, Item item, OptionDetail optionDetail){
        String orderId = createOrderId(requestDto.getItemId());
        Order order = requestDto.toOrder(orderId, user, item, optionDetail);
        order = orderRepository.save(order);
        log.info("Create new order, order_id: {}", orderId);
        return  order;
    }

    private String createOrderId(String itemId){
        Random random = new Random();
        char firstChar = (char) ('A' + random.nextInt(26));
        char secondChar = (char) ('A' + random.nextInt(26));
        return itemId + firstChar + secondChar;
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
                .paymentMethod(payment.getMethod())
                .paidAt(payment.getEndedAt())
                .discountPrice(discount.getDiscountPrice())
                .charge(payment.getCharge())
                .totalPrice(payment.getTotalPrice())
                .build();

        return responseDto;
    }

}
