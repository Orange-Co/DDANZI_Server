package co.orange.ddanzi.service;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.exception.ItemNotFoundException;
import co.orange.ddanzi.common.exception.PaymentNotFoundException;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.domain.order.Payment;
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
import co.orange.ddanzi.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final AuthUtils authUtils;
    private final ItemRepository itemRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public ApiResponse<?> startPayment(CreatePaymentRequestDto requestDto){
        Item item = itemRepository.findById(requestDto.getItemId()).orElseThrow(()-> new ItemNotFoundException());
        log.info("Find item, item_id: {}", requestDto.getItemId());

        if(!item.getStatus().equals(ItemStatus.ON_SALE))
            return ApiResponse.onFailure(Error.ITEM_IS_NOT_ON_SALE,null);

        Payment payment = requestDto.toEntity(item, authUtils.getUser());
        payment = paymentRepository.save(payment);
        log.info("Register payment");

        item.updateStatus(ItemStatus.IN_TRANSACTION);
        log.info("Update item status, item_status: {}", item.getStatus());

        Product product = item.getProduct();
        product.updateStock(product.getStock() - 1);
        log.info("Update stock of product, product_id: {}", product.getId());

        ///////////////////////////////////////////////////////////////////////////////////////////
        // 형변환 해놨음 다시 수정필요
        CreatePaymentResponseDto responseDto = CreatePaymentResponseDto.builder()
                .paymentId(payment.getId().toString())
                .payStatus(payment.getPayStatus())
                .startedAt(payment.getStartedAt())
                .build();
        return ApiResponse.onSuccess(Success.CREATE_PAYMENT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> endPayment(UpdatePaymentRequestDto requestDto){
        ///////////////////////////////////////////////////////////////////////////////////////////
        // 형변환 해놨음 다시 수정필요
        Payment payment = paymentRepository.findById(Long.parseLong(requestDto.getPaymentId())).orElseThrow(()-> new PaymentNotFoundException());
        Item item = payment.getItem();
        Product product = item.getProduct();

        if(!isAvailableToChangePayment(payment)){
            return ApiResponse.onFailure(Error.PAYMENT_CANNOT_CHANGE, null);
        }

        payment.updatePaymentStatusAndEndedAt(requestDto.getPayStatus());
        log.info("Update payment status, status: {}", payment.getPayStatus());

        if(payment.getPayStatus().equals(PayStatus.CANCELLED)||payment.getPayStatus().equals(PayStatus.FAILED)){
            log.info("Payment is failed");
            item.updateStatus(ItemStatus.ON_SALE);
            product.updateStock(product.getStock() + 1);
        }

        ///////////////////////////////////////////////////////////////////////////////////////////
        // 형변환 해놨음 다시 수정필요
        UpdatePaymentResponseDto responseDto = UpdatePaymentResponseDto.builder()
                .paymentId(payment.getId().toString())
                .payStatus(payment.getPayStatus())
                .endedAt(payment.getEndedAt())
                .build();

        return ApiResponse.onSuccess(Success.PATCH_PAYMENT_STATUS_SUCCESS, responseDto);
    }

    public Integer calculateCharge(Integer salePrice){
        return (int) Math.floor(salePrice*0.032);
    }

    private boolean isAvailableToChangePayment(Payment payment){
        User user = authUtils.getUser();
        if(payment.getBuyer().equals(user) && payment.getPayStatus().equals(PayStatus.PENDING))
            return true;
        else
            return false;
    }

    public void deletePaymentOfUser(User user){

    }

}
