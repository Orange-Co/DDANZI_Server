package co.orange.ddanzi.controller;

import co.orange.ddanzi.domain.user.enums.LoginType;
import co.orange.ddanzi.dto.auth.SigninRequestDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    ApiResponse<?> test(@RequestBody SigninRequestDto requestDto){
        return authService.testSignin(requestDto.getToken());
    }

    @PostMapping("/signin")
    ApiResponse<?> signin(@RequestBody SigninRequestDto requestDto) throws JsonProcessingException {
        if(requestDto.getType().equals(LoginType.KAKAO))
            return authService.kakaoSignIn(requestDto.getToken());
        return authService.kakaoSignIn(requestDto.getToken());
    }
}
