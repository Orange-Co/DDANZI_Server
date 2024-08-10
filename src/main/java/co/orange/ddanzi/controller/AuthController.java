package co.orange.ddanzi.controller;

import co.orange.ddanzi.dto.auth.LoginDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signin/test")
    ApiResponse<?> signin(@RequestBody LoginDto requestDto){
        return authService.testSignin(requestDto.getIdToken());
    }
}
