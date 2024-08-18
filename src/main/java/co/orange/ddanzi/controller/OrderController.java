package co.orange.ddanzi.controller;

import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.service.OrderService;
import co.orange.ddanzi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@RestController
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/product/{id}")
    ApiResponse<?> checkOrderProduct(@PathVariable("id") String id) {
        return orderService.checkOrderProduct(id);
    }
}
