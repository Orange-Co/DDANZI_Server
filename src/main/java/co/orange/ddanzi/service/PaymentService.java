package co.orange.ddanzi.service;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.exception.ItemNotFoundException;
import co.orange.ddanzi.common.exception.ProductNotFoundException;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.order.PaymentHistory;
import co.orange.ddanzi.domain.order.enums.OrderStatus;
import co.orange.ddanzi.domain.order.enums.PayStatus;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.product.enums.ItemStatus;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.payment.CreatePaymentRequestDto;
import co.orange.ddanzi.dto.payment.CreatePaymentResponseDto;
import co.orange.ddanzi.dto.payment.UpdatePaymentRequestDto;
import co.orange.ddanzi.dto.payment.UpdatePaymentResponseDto;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.ItemRepository;
import co.orange.ddanzi.repository.PaymentHistoryRepository;
import co.orange.ddanzi.repository.PaymentRepository;
import co.orange.ddanzi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    OrderService orderService;

    @Transactional
    public ApiResponse<?> startPayment(CreatePaymentRequestDto requestDto){
        User buyer = authUtils.getUser();
        Product product = productRepository.findById(requestDto.getProductId()).orElseThrow(ProductNotFoundException::new);
        Item item = itemRepository.findNearestExpiryItem(product).orElseThrow(ItemNotFoundException::new);

        Order newOrder = orderService.createOrderRecord(buyer, item);

        Payment newPayment = requestDto.toEntity(newOrder);
        newPayment = paymentRepository.save(newPayment);
        log.info("Start payment");

        createPaymentHistory(buyer, newPayment);

        CreatePaymentResponseDto responseDto = CreatePaymentResponseDto.builder()
                .orderId(newOrder.getId())
                .payStatus(newPayment.getPayStatus())
                .startedAt(newPayment.getStartedAt())
                .build();
        return ApiResponse.onSuccess(Success.CREATE_PAYMENT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> endPayment(UpdatePaymentRequestDto requestDto){
        User buyer = authUtils.getUser();
        Order order = orderService.getOrderRecord(requestDto.getOrderId());
        Payment payment = paymentRepository.findByOrder(order);
        Item item = order.getItem();
        Product product = item.getProduct();
        if(!isAvailableToChangePayment(buyer, payment)){
            return ApiResponse.onFailure(Error.PAYMENT_CANNOT_CHANGE, null);
        }

        if(item.getStatus().equals(ItemStatus.IN_TRANSACTION)){
            log.info("해당 제품은 이미 결제 되어 새로운 제품을 탐색합니다.");
            Item newItem = itemRepository.findNearestExpiryItem(product).orElse(null);
            if(newItem ==null){
                log.info("환불을 진행합니다.");
                refundPayment(buyer, payment);
            }
        }
        log.info("End payment");

        item.updateStatus(ItemStatus.IN_TRANSACTION);
        log.info("Update item status, item_status: {}", item.getStatus());

        payment.updatePaymentStatusAndEndedAt(requestDto.getPayStatus());
        log.info("Update payment status, status: {}", payment.getPayStatus());

        if(payment.getPayStatus().equals(PayStatus.CANCELLED)||payment.getPayStatus().equals(PayStatus.FAILED)){
            log.info("Payment is failed");
            item.updateStatus(ItemStatus.ON_SALE);
            order.updateStatus(OrderStatus.CANCELLED);
            product.updateStock(product.getStock() + 1);
        }

        else if(payment.getPayStatus().equals(PayStatus.PAID)){
            log.info("Payment is paid!!");
            item.updateStatus(ItemStatus.CLOSED);
            product.updateStock(product.getStock() - 1);
        }

        createPaymentHistory(buyer, payment);

        UpdatePaymentResponseDto responseDto = UpdatePaymentResponseDto.builder()
                .orderId(order.getId())
                .payStatus(payment.getPayStatus())
                .endedAt(payment.getEndedAt())
                .build();

        return ApiResponse.onSuccess(Success.PATCH_PAYMENT_STATUS_SUCCESS, responseDto);
    }

    public Integer calculateCharge(Integer salePrice){
        return (int) Math.floor(salePrice*0.032);
    }

    private void createPaymentHistory(User user, Payment payment){
        PaymentHistory paymentHistory = PaymentHistory.builder()
                .buyerId(user.getId())
                .payStatus(payment.getPayStatus())
                .payment(payment)
                .createAt(LocalDateTime.now())
                .build();
        paymentHistoryRepository.save(paymentHistory);
    }

    private void createPaymentHistoryWithError(User user, Payment payment, String error){
        PaymentHistory paymentHistory = PaymentHistory.builder()
                .buyerId(user.getId())
                .payStatus(payment.getPayStatus())
                .payment(payment)
                .error(error)
                .createAt(LocalDateTime.now())
                .build();
        paymentHistoryRepository.save(paymentHistory);
    }

    private boolean isAvailableToChangePayment(User user, Payment payment){
        return payment.getOrder().getBuyer().equals(user) && payment.getPayStatus().equals(PayStatus.PENDING);
    }

    private void refundPayment(User user, Payment payment){

    }

}
