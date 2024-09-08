package co.orange.ddanzi.service.auth;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.domain.user.Device;
import co.orange.ddanzi.domain.user.PushAlarm;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.enums.LoginType;
import co.orange.ddanzi.domain.user.enums.UserStatus;
import co.orange.ddanzi.dto.auth.RefreshTokenResponseDto;
import co.orange.ddanzi.dto.auth.SigninRequestDto;
import co.orange.ddanzi.dto.auth.SigninResponseDto;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.global.jwt.JwtUtils;
import co.orange.ddanzi.repository.DeviceRepository;
import co.orange.ddanzi.repository.PushAlarmRepository;
import co.orange.ddanzi.repository.UserRepository;
import co.orange.ddanzi.service.TermService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final PushAlarmRepository pushAlarmRepository;

    @Autowired
    TermService termService;

    @Transactional
    public ApiResponse<?> kakaoSignIn(SigninRequestDto requestDto) throws JsonProcessingException {
        log.info("카카오 로그인 진입");
        String email = getKakaoEmail(requestDto.getToken());

        log.info("카카오 이메일 조회 성공 email: {}", email);
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()){
            log.info("카카오 회원 가입 시작");
            kakaoSignUp(email);
            optionalUser = userRepository.findByEmail(email);
            log.info("이용약관 동의여부 저장");
            User user = optionalUser.get();
            connectUserAndDevice(user, requestDto);
            registerFcmToken(user,requestDto.getFcmToken());
        }

        User user = optionalUser.get();

        if(user.getStatus() == UserStatus.DELETE||user.getStatus()== UserStatus.SLEEP)
            user.updateStatus(UserStatus.ACTIVATE);

        SigninResponseDto responseDto = SigninResponseDto.builder()
                .accesstoken(jwtUtils.createAccessToken(email))
                .refreshtoken(jwtUtils.createRefreshToken(email))
                .nickname(user.getNickname())
                .status(user.getStatus())
                .build();
        return ApiResponse.onSuccess(Success.SIGNIN_KAKAO_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> refreshAccessToken(String refreshToken) throws JsonProcessingException {
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

    public void kakaoSignUp(String email) {
        User user = User.builder()
                .email(email)
                .type(LoginType.KAKAO)
                .status(UserStatus.UNAUTHENTICATED)
                .nickname(generateNickname())
                .build();
        userRepository.save(user);
    }

    public void connectUserAndDevice(User user, SigninRequestDto requestDto) {
        Device device = Device.builder()
                .user(user)
                .deviceToken(requestDto.getDevicetoken())
                .type(requestDto.getDeviceType())
                .build();
        deviceRepository.save(device);
    }

    private void registerFcmToken(User user, String fcmToken) {
        PushAlarm pushAlarm = PushAlarm.builder()
                .user(user)
                .fcmToken(fcmToken)
                .build();
        pushAlarmRepository.save(pushAlarm);
    }

    public String getKakaoEmail(String accessToken) throws JsonProcessingException {
        log.info("HTTP 해더 생성");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        log.info("HTTP 요청 보내기");
        HttpEntity<String> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    kakaoUserInfoRequest,
                    String.class
            );

            log.info("응답 상태 코드: {}", response.getStatusCode());
            log.info("응답 본문: {}", response.getBody());

            // responseBody에 있는 정보 꺼내기
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            log.info("카카오 바디 정보 수집 성공");

            String email = jsonNode.get("kakao_account").get("email").asText();
            return email;

        } catch (Exception e) {
            log.error("카카오 API 요청 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }

    private List<String> loadWordsFromFile(String classpath) throws IOException {
        ClassPathResource resource = new ClassPathResource(classpath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            return reader.lines().collect(Collectors.toList());
        }
    }
    private String generateNickname() {
        try {
            List<String> adjectives = loadWordsFromFile("nickname/adjectives.txt");
            List<String> animals = loadWordsFromFile("nickname/animals.txt");
            String selectedAdjectives = adjectives.get(new Random().nextInt(adjectives.size()));
            String selectedAnimals = animals.get(new Random().nextInt(animals.size()));
            int randomSuffix = (int) (Math.random() * 100);
            return selectedAdjectives + selectedAnimals + randomSuffix;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
