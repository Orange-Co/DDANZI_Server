package co.orange.ddanzi.controller;

import co.orange.ddanzi.dto.order.CreateOrderRequestDto;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@RestController
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/product/{id}")
    ApiResponse<?> checkOrderProduct(@PathVariable("id") String id) {
        return orderService.checkOrderProduct(id);
    }

    @PostMapping
    ApiResponse<?> createOrder(@RequestBody CreateOrderRequestDto requestDto){
        return orderService.createOrder(requestDto);
    }
}
