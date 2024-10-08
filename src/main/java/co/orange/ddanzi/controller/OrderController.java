package co.orange.ddanzi.controller;

import co.orange.ddanzi.dto.order.SaveOrderRequestDto;
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
    ApiResponse<?> saveOrder(@RequestBody SaveOrderRequestDto requestDto){
        return orderService.saveOrder(requestDto);
    }

    @GetMapping("/{id}")
    ApiResponse<?> getOrder(@PathVariable("id") String id){
        return orderService.getOrder(id);
    }

    @PatchMapping("/{id}/buy")
    ApiResponse<?> confirmedOrderToBuy(@PathVariable("id") String id){
        return orderService.confirmedOrderToBuy(id);
    }

    @PatchMapping("/{id}/sale")
    ApiResponse<?> confirmedOrderToSale(@PathVariable("id") String id){
        return orderService.confirmedOrderToSale(id);
    }
}
