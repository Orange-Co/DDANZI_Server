package co.orange.ddanzi.global.firebase;

import co.orange.ddanzi.domain.user.enums.FcmCase;
import co.orange.ddanzi.repository.PushAlarmRepository;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class FirebaseUtils {
    private final PushAlarmRepository pushAlarmRepository;

    public String sendMessage(String fcmToken, FcmCase fcmCase) {
        try {
            log.info("Sending FCM message: {}", fcmCase.getTitle());
            return FirebaseMessaging.getInstance().send(makeMessage(fcmToken , fcmCase));
        } catch (FirebaseMessagingException e) {
            log.error("FirebaseMessagingException occurred: {}", e.getMessage());
            return  "FCM 메시지 전송 실패: " + e.getMessage();
        } catch (Exception e) {
            log.info(e.getMessage());
            return e.getMessage();
        }
    }

    public String sendAdminMessage(String fcmToken,  String title, String body) {
        try {
            log.info("관리자 Sending FCM message: {}", title);
            return FirebaseMessaging.getInstance().send(makeAdminMessage(fcmToken , title, body));
        } catch (FirebaseMessagingException e) {
            log.error("관리자 FirebaseMessagingException occurred: {}", e.getMessage());
            return  "관리자 FCM 메시지 전송 실패: " + e.getMessage();
        } catch (Exception e) {
            log.info(e.getMessage());
            return e.getMessage();
        }
    }


    public Message makeMessage(String fcmSendDto, FcmCase fcmCase) {
        Notification notification = Notification
                .builder()
                .setTitle(fcmCase.getTitle())
                .setBody(fcmCase.getBody())
                .build();
        return Message
                .builder()
                .setNotification(notification)
                .putData("title", fcmCase.getTitle())
                .putData("body", fcmCase.getBody())
                .setToken(fcmSendDto)
                .build();
    }

    public Message makeAdminMessage(String fcmSendDto, String title, String body) {
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();
        return Message
                .builder()
                .setNotification(notification)
                .putData("title", title)
                .putData("body",body)
                .setToken(fcmSendDto)
                .build();
    }


    public MulticastMessage makeMessages(List<String> targetTokens, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
        return MulticastMessage.builder()
                .setNotification(notification)
                .addAllTokens(targetTokens)
                .build();

    }


    //    @Value("${firebase.credentials.location}")
//    private String location;

//    private String getAccessToken(){
//       try{
//           GoogleCredentials googleCredentials = GoogleCredentials
//                   .fromStream(new ClassPathResource(location).getInputStream())
//                   .createScoped(List.of("<https://www.googleapis.com/auth/cloud-platform>"));
//
//           googleCredentials.refreshIfExpired();
//           return googleCredentials.getAccessToken().getTokenValue();
//       }
//       catch (IOException e) {
//           e.printStackTrace();
//           return null;
//       }
//    }

}
