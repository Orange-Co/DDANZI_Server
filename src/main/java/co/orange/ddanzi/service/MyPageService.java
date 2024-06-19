package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {
    private final UserRepository userRepository;

    @Transactional
    public ApiResponse<?> getMyPage(){
        User user = userRepository.findById(1L).orElse(null);
        String nickname = user.getNickname();
        Map<String, Object> response = new HashMap<>();
        response.put("nickname", nickname);
        return ApiResponse.onSuccess(Success.GET_MY_PAGE_INFO_SUCCESS, response);
    }
}
