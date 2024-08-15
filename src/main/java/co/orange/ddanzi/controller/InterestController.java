package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/interest")
public class InterestController {
    private final InterestService interestService;

    @PostMapping("/{productId}")
    ApiResponse<?> addInterest(@PathVariable String productId) {
        return interestService.addInterest(productId);
    }

    @DeleteMapping("/{productId}")
    ApiResponse<?> deleteInterest(@PathVariable String productId) {
        return interestService.deleteInterest(productId);
    }
}
