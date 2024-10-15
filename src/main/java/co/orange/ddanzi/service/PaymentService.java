package co.orange.ddanzi.service;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.exception.ItemNotFoundException;
import co.orange.ddanzi.common.exception.ProductNotFoundException;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.order.PaymentChargeInfo;
import co.orange.ddanzi.domain.order.enums.OrderStatus;
import co.orange.ddanzi.domain.order.enums.PayStatus;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.product.enums.ItemStatus;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.payment.*;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.*;
import co.orange.ddanzi.service.common.FcmService;
import co.orange.ddanzi.service.common.HistoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentChargeInfoRepository paymentChargeInfoRepository;

    private final OrderService orderService;
    private final HistoryService historyService;
    private final FcmService fcmService;

    @Value("${ddanzi.portone.access-key}")
    private String accessKey;
    @Value("${ddanzi.portone.access-secret}")
    private String accessSecret;

    @Transactional
    public ApiResponse<?> startPayment(CreatePaymentRequestDto requestDto){
        User buyer = authUtils.getUser();
        Product product = productRepository.findById(requestDto.getProductId()).orElseThrow(ProductNotFoundException::new);
        Item item = itemRepository.findNearestExpiryItem(product).orElseThrow(ItemNotFoundException::new);

        Order newOrder = orderService.createOrderRecord(buyer, item);
        historyService.createOrderHistory(newOrder);

        Payment newPayment = requestDto.toEntity(newOrder);
        newPayment = paymentRepository.save(newPayment);
        log.info("Start payment");

        historyService.createPaymentHistory(buyer, newPayment);

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

        if(item.getStatus().equals(ItemStatus.IN_TRANSACTION) || item.getStatus().equals(ItemStatus.DELETED)){
            log.info("해당 제품은 거래불가능하여 새로운 제품을 탐색합니다.");
            Item newItem = itemRepository.findNearestExpiryItem(product).orElse(null);
            if(newItem == null){
                log.info("환불을 진행합니다.");
                try {
                    refundPayment(buyer, order, "현재 남은 재고가 없어 고객에게 결제 금액 환불합니다.");
                    payment.updatePaymentStatusAndEndedAt(PayStatus.CANCELLED);
                    historyService.createPaymentHistoryWithError(buyer, payment, "재고 없음- 환불 처리 성공");
                    return ApiResponse.onFailure(Error.NO_ITEM_ON_SALE, Map.of("orderId", order.getId()));
                }catch (Exception e){
                    historyService.createPaymentHistoryWithError(buyer, payment, "재고 없음 - 환불 처리 실패");
                    return ApiResponse.onFailure(Error.REFUND_FAILED, Map.of("orderId", order.getId()));
                }
            }
            else{
                log.info("새로운 제품을 할당하였습니다.");
                order.updateItem(newItem);
                item = order.getItem();
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
            fcmService.sendMessageToAdmins("⚠️관리자 알림: 구매실행", "결제가 실행되었습니다. orderId:" + order.getId());
        }

        historyService.createPaymentHistory(buyer, payment);

        UpdatePaymentResponseDto responseDto = UpdatePaymentResponseDto.builder()
                .orderId(order.getId())
                .payStatus(payment.getPayStatus())
                .endedAt(payment.getEndedAt())
                .build();

        return ApiResponse.onSuccess(Success.PATCH_PAYMENT_STATUS_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> refundTest(UpdatePaymentRequestDto requestDto){
        User buyer = authUtils.getUser();
        Order order = orderService.getOrderRecord(requestDto.getOrderId());
        Payment payment = paymentRepository.findByOrder(order);
        refundPayment(buyer, order, "테스트용 환불입니다.");
        return ApiResponse.onSuccess(Success.SUCCESS, true);
    }

    public Integer calculateCharge(Integer salePrice){
        PaymentChargeInfo chargeInfo = paymentChargeInfoRepository.findById(1).orElseThrow(ItemNotFoundException::new);
        return (int) Math.floor(salePrice * chargeInfo.getCharge());
    }

    private boolean isAvailableToChangePayment(User user, Payment payment){
        return payment.getOrder().getBuyer().equals(user) && payment.getPayStatus().equals(PayStatus.PENDING);
    }

    public String getPortOneAccessToken(){
        String baseUrl = "https://api.iamport.kr/users/getToken";
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .toUriString();
        log.info("포트원 Access key를 받아오는 url 생성, url-> {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        PortOneTokenRequestDto requestBody = PortOneTokenRequestDto.builder()
                .imp_key(accessKey)
                .imp_secret(accessSecret)
                .build();

        HttpEntity<PortOneTokenRequestDto> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        log.info("포트원 Access key 요청 생성");
        ResponseEntity<PortOneTokenResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, PortOneTokenResponseDto.class);
        log.info("포트원 Access key Get 성공");
        return response.getBody().getResponse().getAccess_token();
    }

    public void refundPayment(User user, Order order, String reason){
        if(!user.equals(order.getBuyer()))
            throw new RuntimeException("결제자와 요청자가 다르므로 환불이 어렵습니다.");
        try{
            String baseUrl = "https://api.iamport.kr/payments/cancel";
            String url = UriComponentsBuilder.fromUriString(baseUrl)
                    .toUriString();
            log.info("결제 취소 url 생성, url-> {}", url);

            String key = getPortOneAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", key);

            RefundRequestDto requestDto = RefundRequestDto.builder()
                    .merchant_uid(order.getId())
                    .reason(reason)
                    .build();

            HttpEntity<Object> entity = new HttpEntity<>(requestDto, headers);
            log.info("헤더 및 request body 생성");

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(url, entity, String.class);
            log.info("결제 취소 api 호출");
            fcmService.sendMessageToAdmins("⚠️관리자 알림: 환불실행", "중복 결제로 인해 환불되었습니다. orderId:" + order.getId());
        }catch (Exception e){
            log.info("환불 실패");
            fcmService.sendMessageToAdmins("⚠️관리자 알림: 환불 실패", "환불에 실패했습니다. orderId:" + order.getId());
        }

    }

}
