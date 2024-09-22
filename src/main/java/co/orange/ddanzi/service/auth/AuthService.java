package co.orange.ddanzi.service.auth;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.domain.user.Authentication;
import co.orange.ddanzi.domain.user.Device;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.enums.LoginType;
import co.orange.ddanzi.domain.user.enums.UserStatus;
import co.orange.ddanzi.dto.auth.*;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.global.jwt.JwtUtils;
import co.orange.ddanzi.repository.AuthenticationRepository;
import co.orange.ddanzi.repository.DeviceRepository;
import co.orange.ddanzi.repository.UserRepository;
import co.orange.ddanzi.service.FcmService;
import co.orange.ddanzi.service.TermService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtUtils jwtUtils;
    private final AuthUtils authUtils;
    private final OAuthService oAuthService;
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private final DeviceRepository deviceRepository;

    private final FcmService fcmService;
    private final TermService termService;

    @Transactional
    public ApiResponse<?> testSignin(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();
        SigninResponseDto responseDto = SigninResponseDto.builder()
                .accesstoken(jwtUtils.createAccessToken(user.getEmail()))
                .nickname(user.getNickname())
                .build();
        return ApiResponse.onSuccess(Success.SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> signin(SigninRequestDto requestDto) throws JsonProcessingException {
        User user = authenticateUser(requestDto);
        connectUserAndDevice(user, requestDto);
        fcmService.registerFcmToken(user,requestDto.getFcmToken());
        checkInactiveUser(user);

        SigninResponseDto responseDto = SigninResponseDto.builder()
                .accesstoken(jwtUtils.createAccessToken(user.getEmail()))
                .refreshtoken(jwtUtils.createRefreshToken(user.getEmail()))
                .nickname(user.getNickname())
                .status(user.getStatus())
                .build();
        return ApiResponse.onSuccess(Success.SIGNIN_KAKAO_SUCCESS, responseDto);
    }




    @Transactional
    public ApiResponse<?> verify(VerifyRequestDto requestDto){
        User user = authUtils.getUser();
        log.info("유저 정보 가져옴 user_id -> {}", user.getId());

        Authentication authentication = user.getAuthentication();
        if(authentication != null) {
            return verifyExistingAuthentication(user, requestDto);
        }
        else{
            String phone = formatPhone(requestDto.getPhone());

            authentication = requestDto.toEntity(user, phone);
            authentication = authenticationRepository.save(authentication);
            log.info("본인인증 완료 authentication_id -> {}", authentication.getId());

            user.setAuthentication(UserStatus.ACTIVATE,authentication);
            log.info("회원 정보 변경 완료 user_authentication_id -> {}", user.getAuthentication().getId());

            termService.createUserAgreements(user, requestDto.getIsAgreedMarketingTerm());
            return ApiResponse.onSuccess(Success.CREATE_AUTHENTICATION_SUCCESS, setVerifyResponse(user));
        }
    }

    @Transactional
    public ApiResponse<?> refreshAccessToken(String refreshToken) throws JsonProcessingException {
        log.info("리프레시 토큰으로 엑세스 토큰 발급 시작");
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ApiResponse.onFailure(Error.REFRESH_TOKEN_IS_NULL, Map.of("refreshtoken", refreshToken));
        }

        String email = jwtUtils.getIdFromRefreshToken(refreshToken);

        if (!jwtUtils.isValidRefreshToken(email, refreshToken)) {
            return ApiResponse.onFailure(Error.REFRESH_TOKEN_EXPIRED, Map.of("refreshtoken", refreshToken));
        }

        RefreshTokenResponseDto responseDto = RefreshTokenResponseDto.builder()
                .accesstoken(jwtUtils.createAccessToken(email))
                .refreshtoken(jwtUtils.createRefreshToken(email))
                .build();

        return ApiResponse.onSuccess(Success.REFRESH_ACCESS_TOKEN_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> withdraw(){
        User user = authUtils.getUser();
        user.updateStatus(UserStatus.DELETE);
        return ApiResponse.onSuccess(Success.DELETE_USER_SUCCESS, Map.of("nickname", user.getNickname()));
    }

    private ApiResponse<?> verifyExistingAuthentication(User user, VerifyRequestDto requestDto) {
        if (!user.getAuthentication().getCi().equals(requestDto.getCi())) {
            return ApiResponse.onFailure(Error.AUTHENTICATION_CANNOT_CHANGE, null);
        }
        return ApiResponse.onSuccess(Success.CREATE_AUTHENTICATION_SUCCESS, setVerifyResponse(user));
    }

    private User authenticateUser(SigninRequestDto requestDto) throws JsonProcessingException {
        if (requestDto.getType().equals(LoginType.KAKAO)) {
            return oAuthService.kakaoSignIn(requestDto);
        } else {
            return oAuthService.appleSignin(requestDto);
        }
    }

    public void checkInactiveUser(User user){
        if(user.getStatus() == UserStatus.DELETE||user.getStatus()== UserStatus.SLEEP)
            user.updateStatus(UserStatus.UNAUTHENTICATED);

    }

    public void connectUserAndDevice(User user, SigninRequestDto requestDto) {
        log.info("유저의 디바이스 토큰 저장");
        Device device = Device.builder()
                .user(user)
                .deviceToken(requestDto.getDevicetoken())
                .deviceType(requestDto.getDeviceType())
                .build();
        deviceRepository.save(device);
    }

    private String formatPhone(String phone) {
        return phone.replace("-", "").replace(" ", "");
    }

    private VerifyResponseDto setVerifyResponse(User user) {
        return VerifyResponseDto.builder()
                .nickname(user.getNickname())
                .phone(user.getAuthentication().getPhone())
                .status(user.getStatus())
                .build();
  }
}


