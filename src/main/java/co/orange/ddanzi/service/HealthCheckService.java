package co.orange.ddanzi.service;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.domain.ServerStatus;
import co.orange.ddanzi.repository.ServerStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HealthCheckService {
    private final ServerStatusRepository serverStatusRepository;

    public ApiResponse<?> checkStatus(){
        ServerStatus serverStatus = serverStatusRepository.findTopByOrderByIdDesc();
        if(serverStatus.getIsActive()){
            return ApiResponse.onSuccess(Success.SUCCESS, true);
        }
        else{
            return ApiResponse.onFailure(Error.ERROR, false);
        }
    }
}
