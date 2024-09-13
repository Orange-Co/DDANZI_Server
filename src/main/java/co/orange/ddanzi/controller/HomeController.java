package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/home")
public class HomeController {
    private final HomeService homeService;

    @GetMapping
    public ApiResponse<?> home(@PageableDefault(size = 12) Pageable pageable) {
        return homeService.getProductList(pageable);
    }

    @GetMapping( "/product/{id}")
    public ApiResponse<?> homeDetail(@RequestHeader("devicetoken") String devicetoken,
                                     @PathVariable String id) {
        return homeService.getProductDetail(devicetoken, id);
    }
}
