package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class HealthCheckController {

    @GetMapping("/health-check")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> healthCheck() {
        return ApiResponse.onSuccess(Success.SUCCESS,null);
    }

}
