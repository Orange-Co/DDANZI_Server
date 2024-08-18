package co.orange.ddanzi.service;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.exception.ItemNotFoundException;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.product.enums.ItemStatus;
import co.orange.ddanzi.dto.payment.CreatePaymentRequestDto;
import co.orange.ddanzi.dto.payment.CreatePaymentResponseDto;
import co.orange.ddanzi.dto.payment.UpdatePaymentRequestDto;
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

    private final ItemRepository itemRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public ApiResponse<?> startPayment(CreatePaymentRequestDto requestDto){
        Item item = itemRepository.findById(requestDto.getItemId()).orElseThrow(()-> new ItemNotFoundException());
        log.info("Find item, item_id: {}", requestDto.getItemId());

        if(!item.getStatus().equals(ItemStatus.ON_SALE))
            return ApiResponse.onFailure(Error.ITEM_IS_NOT_ON_SALE,null);

        Payment payment = requestDto.toEntity(item);
        payment = paymentRepository.save(payment);
        log.info("Register payment");

        item.updateStatus(ItemStatus.IN_TRANSACTION);
        log.info("Update item status, item_status: {}", item.getStatus());

        Product product = item.getProduct();
        product.updateStock(product.getStock() - 1);
        log.info("Update stock of product, product_id: {}", product.getId());

        CreatePaymentResponseDto responseDto = CreatePaymentResponseDto.builder()
                .paymentId(payment.getId())
                .payStatus(payment.getPayStatus())
                .startedAt(payment.getStartedAt())
                .build();
        return ApiResponse.onSuccess(Success.CREATE_PAYMENT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> endPayment(UpdatePaymentRequestDto requestDto){
        return ApiResponse.onSuccess(Success.PATCH_PAYMENT_STATUS_SUCCESS, null);
    }
}
