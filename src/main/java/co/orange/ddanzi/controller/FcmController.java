package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.dto.fcm.FcmSendDto;
import co.orange.ddanzi.service.FcmService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("/fcm/test")
    ApiResponse<?> testFcm(@RequestBody FcmSendDto requestDto) throws FirebaseMessagingException {
        return fcmService.testSendMessage(requestDto);
    }

    @PostMapping("/fcm/report")
    ApiResponse<?> reportNotification(@RequestBody FcmSendDto requestDto) throws FirebaseMessagingException {
        return fcmService.reportNotification(requestDto);
    }
}
