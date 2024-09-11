package co.orange.ddanzi.service;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.domain.user.PushAlarm;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.enums.FcmCase;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.dto.fcm.FcmSendDto;
import co.orange.ddanzi.global.firebase.FirebaseUtils;
import co.orange.ddanzi.repository.PushAlarmRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmService {
    private final FirebaseUtils firebaseUtils;
    private final PushAlarmRepository pushAlarmRepository;

    @Transactional
    public ApiResponse<?> testSendMessage(FcmSendDto requestDto) throws FirebaseMessagingException {
        try {
            log.info("Sending FCM message: {}", FirebaseMessaging.getInstance().send(firebaseUtils.makeMessage(requestDto.getFcmToken(), FcmCase.A1)));
            FirebaseMessaging.getInstance().send(firebaseUtils.makeMessage(requestDto.getFcmToken(), FcmCase.A1));
        } catch (FirebaseMessagingException e) {
            log.error("FirebaseMessagingException occurred: {}", e.getMessage());
            return ApiResponse.onFailure(Error.ERROR, "FCM 메시지 전송 실패: " + e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            ApiResponse.onFailure(Error.ERROR, e.getMessage());
        }
        return ApiResponse.onSuccess(Success.SUCCESS, Map.of("fcmToken", requestDto.getFcmToken() ));
    }

    @Transactional
    public ApiResponse<?> reportNotification(FcmSendDto requestDto) throws FirebaseMessagingException {
        firebaseUtils.sendMessage(requestDto.getFcmToken(), FcmCase.A4);
        return ApiResponse.onSuccess(Success.SUCCESS, Map.of("title", FcmCase.A4.getTitle()));
    }

    public void registerFcmToken(User user, String fcmToken) {
        PushAlarm pushAlarm = pushAlarmRepository.findByUser(user).orElse(null);
        if(pushAlarm == null) {
            PushAlarm newPushAlarm = PushAlarm.builder()
                    .user(user)
                    .fcmToken(fcmToken)
                    .build();
            pushAlarmRepository.save(newPushAlarm);
        }
        else {
            pushAlarm.updateToken(fcmToken);
        }
    }

    public boolean sendMessageToUser(User user, FcmCase fcmCase) {
        log.info("Sending FCM message: {}", fcmCase.getTitle());
        PushAlarm pushAlarm = pushAlarmRepository.findByUser(user).orElse(null);
        if(pushAlarm == null)
            return false;
        String fcmToken = pushAlarm.getFcmToken();
        firebaseUtils.sendMessage(fcmToken, fcmCase);
        return true;
    }
}
