package co.orange.ddanzi.controller;

import co.orange.ddanzi.domain.user.enums.LoginType;
import co.orange.ddanzi.dto.auth.SigninRequestDto;
import co.orange.ddanzi.dto.auth.VerifyRequestDto;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.service.auth.AuthService;
import co.orange.ddanzi.service.auth.OAuthService;
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
    private final OAuthService oAuthService;

    @PostMapping("/signin/test")
    ApiResponse<?> test(@RequestBody SigninRequestDto requestDto){
        return authService.testSignin(requestDto.getToken());
    }

    @PostMapping("/signin")
    ApiResponse<?> signin(@RequestBody SigninRequestDto requestDto) throws JsonProcessingException {
        if(requestDto.getType().equals(LoginType.KAKAO))
            return oAuthService.kakaoSignIn(requestDto.getToken());
        return oAuthService.kakaoSignIn(requestDto.getToken());
    }

    @PostMapping("/refreshtoken")
    ApiResponse<?> refreshAccessToken(@RequestBody String refreshToken) throws JsonProcessingException{
        return oAuthService.refreshAccessToken(refreshToken);
    }

    @PostMapping("/verification")
    ApiResponse<?> verify(@RequestBody VerifyRequestDto requestDto) {
        return authService.verify(requestDto);
    }

}
