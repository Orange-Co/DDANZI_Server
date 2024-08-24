package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.domain.user.enums.LoginType;
import co.orange.ddanzi.dto.auth.RefreshTokenRequestDto;
import co.orange.ddanzi.dto.auth.SigninRequestDto;
import co.orange.ddanzi.dto.auth.VerifyRequestDto;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.global.jwt.JwtUtils;
import co.orange.ddanzi.service.auth.AuthService;
import co.orange.ddanzi.service.auth.OAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final OAuthService oAuthService;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin/test")
    ApiResponse<?> test(@RequestBody SigninRequestDto requestDto){
        return authService.testSignin(requestDto.getToken());
    }

    @PostMapping("/signin")
    ApiResponse<?> signin(@RequestBody SigninRequestDto requestDto) throws JsonProcessingException {
        if(requestDto.getType().equals(LoginType.KAKAO))
            return oAuthService.kakaoSignIn(requestDto);
        return oAuthService.kakaoSignIn(requestDto);
    }

    @PostMapping("/refreshtoken")
    ApiResponse<?> refreshAccessToken(@RequestBody RefreshTokenRequestDto requestDto) throws JsonProcessingException{
        return oAuthService.refreshAccessToken(requestDto.getRefreshtoken());
    }

    @PostMapping("/logout")
    ApiResponse<?> logout(HttpServletRequest request){
        String accesstoken = jwtUtils.resolveJWT(request);
        if (accesstoken == null) {
            return ApiResponse.onFailure(Error.JWT_TOKEN_NOT_EXISTS, null);
        }

        // 블랙리스트에 토큰 추가 (로그아웃 처리)
        jwtUtils.setBlackList(accesstoken);

        return ApiResponse.onSuccess(Success.LOGOUT_SUCCESS, true);
    }

    @PostMapping("/verification")
    ApiResponse<?> verify(@RequestBody VerifyRequestDto requestDto) {
        return authService.verify(requestDto);
    }

    @DeleteMapping("/withdraw")
    ApiResponse<?> withdraw(){
        return authService.withdraw();
    }

}
