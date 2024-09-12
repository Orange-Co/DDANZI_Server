package co.orange.ddanzi.service.auth;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.enums.LoginType;
import co.orange.ddanzi.domain.user.enums.UserStatus;
import co.orange.ddanzi.dto.auth.SigninRequestDto;
import co.orange.ddanzi.dto.oauth.AppleIdTokenPayload;
import co.orange.ddanzi.dto.oauth.AppleProperties;
import co.orange.ddanzi.dto.oauth.AppleSocialTokenInfoResponse;
import co.orange.ddanzi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final AppleProperties appleProperties;

    /**
     * KAKAO LOGIN
     */
    @Transactional
    public User kakaoSignIn(SigninRequestDto requestDto) throws JsonProcessingException {
        log.info("카카오 로그인 진입");
        String email = getKakaoEmail(requestDto.getToken());

        log.info("카카오 이메일 조회 성공 email: {}", email);
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null){
            return kakaoSignUp(email);
        }
        return user;
    }

    public User kakaoSignUp(String email) {
        User user = User.builder()
                .email(email)
                .type(LoginType.KAKAO)
                .status(UserStatus.UNAUTHENTICATED)
                .nickname(generateNickname())
                .build();
        return userRepository.save(user);
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

    /**
     * APPLE LOGIN
     */
    @Transactional
    public User appleSignin(SigninRequestDto requestDto) throws JsonProcessingException {
        log.info("애플 로그인 진입");
        String email = getAppleEmail(requestDto.getToken());
        log.info("애플 이메일 조회 성공: {}", email);
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.info("애플 회원가입 시작");
            return appleSignup(email);
        }
        return user;
    }

    public User appleSignup(String email){
        User newUser = User.builder()
                .email(email)
                .nickname(generateNickname())
                .status(UserStatus.UNAUTHENTICATED)
                .type(LoginType.APPLE)
                .build();
        return userRepository.save(newUser);
    }

    public String getAppleEmail(String authorizationCode) {
        log.info("애플 서버 통신 준비");
        String url = appleProperties.getAudience() + "/auth/token";
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("client_id", appleProperties.getClientId());
        requestParams.add("client_secret", generateClientSecret());
        requestParams.add("grant_type", appleProperties.getGrantType());
        requestParams.add("code", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestParams, headers);

        ResponseEntity<AppleSocialTokenInfoResponse> response = restTemplate.postForEntity(
                url,
                requestEntity,
                AppleSocialTokenInfoResponse.class
        );
        log.info("애플 서버 통신 완료");
        log.info("응답 상태 코드: {}", response.getStatusCode());
        log.info("응답 본문: {}", response.getBody());

        String idToken = response.getBody().getIdToken();
        AppleIdTokenPayload payload = decodePayload(idToken, AppleIdTokenPayload.class);

        return payload.getEmail();
    }

    private String generateClientSecret() {

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleProperties.getKeyId())
                .setIssuer(appleProperties.getTeamId())
                .setAudience(appleProperties.getAudience())
                .setSubject(appleProperties.getClientId())
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuedAt(new Date())
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getPrivateKey());

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC"); // Elliptic Curve 키 타입 (ES256에 맞게)

            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }


    // TokenDecoder 메소드를 GetMemberInfoService 내부에 통합
    private <T> T decodePayload(String token, Class<T> targetClass) {
        String[] tokenParts = token.split("\\.");
        String payloadJWT = tokenParts[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(payloadJWT));
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(payload, targetClass);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding token payload", e);
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
