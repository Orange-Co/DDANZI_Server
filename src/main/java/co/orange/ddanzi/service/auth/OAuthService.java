package co.orange.ddanzi.service.auth;

import co.orange.ddanzi.domain.user.Authentication;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.enums.LoginType;
import co.orange.ddanzi.domain.user.enums.UserStatus;
import co.orange.ddanzi.dto.auth.SigninResponseDto;
import co.orange.ddanzi.dto.auth.VerifyRequestDto;
import co.orange.ddanzi.dto.auth.VerifyResponseDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.global.config.jwt.AuthUtils;
import co.orange.ddanzi.global.config.jwt.JwtUtils;
import co.orange.ddanzi.repository.AuthenticationRepository;
import co.orange.ddanzi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Transactional
    public ApiResponse<?> kakaoSignIn(String token) throws JsonProcessingException {
        log.info("카카오 로그인 진입");
        String email = getKakaoEmail(token);
        log.info("카카오 이메일 조회 성공 email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()){
            log.info("카카오 회원 가입 시작");
            kakaoSignUp(email);
            user = userRepository.findByEmail(email);
        }

        SigninResponseDto responseDto = SigninResponseDto.builder()
                .accesstoken(jwtUtils.createAccessToken(email))
                .refreshtoken(jwtUtils.createRefreshToken(email))
                .nickname(user.get().getNickname())
                .build();
        return ApiResponse.onSuccess(Success.SIGNIN_KAKAO_SUCCESS, responseDto);
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
