package co.orange.ddanzi.service;

import co.orange.ddanzi.common.exception.ItemNotFoundException;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.dto.payment.CreatePaymentRequestDto;
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
        Item item = itemRepository.findById(requestDto.getItemId()).orElseThrow(()->new ItemNotFoundException());
        Payment payment = requestDto.toEntity(item);
        payment = paymentRepository.save(payment);
        return ApiResponse.onSuccess(Success.CREATE_PAYMENT_SUCCESS, null);
    }
}
