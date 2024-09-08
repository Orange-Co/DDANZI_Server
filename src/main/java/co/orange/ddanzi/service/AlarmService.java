package co.orange.ddanzi.service;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class AlarmService {
    private final AuthUtils authUtils;
    private final AlarmRepository alarmRepository;

    public ApiResponse<?> getAlarms(){
        User user = authUtils.getUser();
        return ApiResponse.onSuccess(Success.GET_ALARM_LIST_SUCCESS,null);
    }
}
