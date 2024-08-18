package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.dto.payment.CreatePaymentRequestDto;
import co.orange.ddanzi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/start")
    ApiResponse<?> startPayment(@RequestBody CreatePaymentRequestDto requestDto){
        return paymentService.startPayment(requestDto);
    }
}
