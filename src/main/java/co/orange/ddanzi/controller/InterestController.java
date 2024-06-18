package co.orange.ddanzi.controller;

import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/interest")
public class InterestController {
    private final InterestService interestService;

    @PostMapping("{productId}")
    ApiResponse<?> addInterest(@PathVariable Long productId) {
        return interestService.addInterest(productId);
    }

}
