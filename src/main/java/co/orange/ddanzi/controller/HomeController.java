package co.orange.ddanzi.controller;

import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/home")
public class HomeController {
    private final HomeService homeService;

    @GetMapping
    public ApiResponse<?> home() {
        return homeService.getProductList();
    }

    @GetMapping("/product/{id}")
    public ApiResponse<?> homeDetail(@PathVariable String id) {
        return homeService.getProductDetail(id);
    }
}
