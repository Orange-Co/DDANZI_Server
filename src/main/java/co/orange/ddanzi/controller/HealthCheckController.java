package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.service.HealthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/api/v1/health")
    public ApiResponse<?> checkStatus() {
        return healthCheckService.checkStatus();

    }

}
