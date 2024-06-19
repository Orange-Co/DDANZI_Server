package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.user.Authentication;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.setting.SettingResponseDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.AuthenticationRepository;
import co.orange.ddanzi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SettingService {
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;

    @Transactional
    public ApiResponse<?> getSetting(){
        User user = userRepository.findById(1L).orElse(null);
        Authentication authentication = authenticationRepository.findById(user.getId()).orElse(null);

        String name = null; String phone = null;
        if(authentication != null){
            name = authentication.getName();
            phone = authentication.getPhone();
        }

        SettingResponseDto responseDto = SettingResponseDto.builder()
                .name(name)
                .nickname(user.getNickname())
                .phone(phone)
                .build();

        return ApiResponse.onSuccess(Success.GET_SETTING_SCREEN_SUCCESS, responseDto);
    }
}
