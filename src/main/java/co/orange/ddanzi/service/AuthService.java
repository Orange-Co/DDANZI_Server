package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.auth.AuthResponseDto;
import co.orange.ddanzi.global.common.error.Error;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.global.config.jwt.JwtUtils;
import co.orange.ddanzi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public ApiResponse<?> testSignin(String idToken){

        Optional<User> optionalUser = userRepository.findByLoginId(idToken);
        if(optionalUser.isEmpty()){
            return ApiResponse.onFailure(Error.ERROR, null);
        }
        User user = optionalUser.get();

        AuthResponseDto responseDto = AuthResponseDto.builder()
                .accesstoken(jwtUtils.createAccessToken(user.getLoginId()))
                .nickname(user.getNickname())
                .build();
        return ApiResponse.onSuccess(Success.SUCCESS, responseDto);
    }
}


