package co.orange.ddanzi.service.auth;

import co.orange.ddanzi.domain.user.Authentication;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.enums.UserStatus;
import co.orange.ddanzi.dto.auth.AuthResponseDto;
import co.orange.ddanzi.dto.auth.VerifyRequestDto;
import co.orange.ddanzi.dto.auth.VerifyResponseDto;
import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.global.jwt.JwtUtils;
import co.orange.ddanzi.repository.AuthenticationRepository;
import co.orange.ddanzi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private final AuthUtils authUtils;

    @Transactional
    public ApiResponse<?> testSignin(String idToken){

        Optional<User> optionalUser = userRepository.findByEmail(idToken);
        if(optionalUser.isEmpty()){
            return ApiResponse.onFailure(Error.ERROR, null);
        }
        User user = optionalUser.get();

        AuthResponseDto responseDto = AuthResponseDto.builder()
                .accesstoken(jwtUtils.createAccessToken(user.getEmail()))
                .nickname(user.getNickname())
                .build();
        return ApiResponse.onSuccess(Success.SUCCESS, responseDto);
    }


    @Transactional
    public ApiResponse<?> verify(VerifyRequestDto requestDto){
        User user = authUtils.getUser();
        log.info("유저 정보 가져옴 user_id -> {}", user.getId());

        if(user.getAuthentication() != null)
            return ApiResponse.onFailure(Error.AUTHENTICATION_ALREADY_EXISTS, Map.of("user name", user.getAuthentication().getName()));

        String phone = requestDto.getPhone().replace("-", "").replace(" ","");
        Authentication newAuthentication = requestDto.toEntity(user, phone);
        newAuthentication = authenticationRepository.save(newAuthentication);
        log.info("본인인증 완료 authentication_id -> {}", newAuthentication.getId());

        user.setAuthentication(UserStatus.ACTIVATE,newAuthentication);
        log.info("회원 정보 변경 완료 user_authentication_id -> {}", user.getAuthentication().getId());

        VerifyResponseDto responseDto = VerifyResponseDto.builder()
                .nickname(user.getNickname())
                .phone(newAuthentication.getPhone())
                .status(user.getStatus())
                .build();
        return ApiResponse.onSuccess(Success.CREATE_AUTHENTICATION_SUCCESS, responseDto);
    }

}


