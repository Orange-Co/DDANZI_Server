package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.dto.payment.CreatePaymentRequestDto;
import co.orange.ddanzi.dto.payment.UpdatePaymentRequestDto;
import co.orange.ddanzi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/start")
    ApiResponse<?> startPayment(@RequestBody CreatePaymentRequestDto requestDto){
        return paymentService.startPayment(requestDto);
    }

    @PatchMapping("/end")
    ApiResponse<?> endPayment(@RequestBody UpdatePaymentRequestDto requestDto){
        return paymentService.endPayment(requestDto);
    }

    @PostMapping("/test")
    ApiResponse<?> refundTest(@RequestBody UpdatePaymentRequestDto requestDto){
        return paymentService.refundTest(requestDto);
    }
}
