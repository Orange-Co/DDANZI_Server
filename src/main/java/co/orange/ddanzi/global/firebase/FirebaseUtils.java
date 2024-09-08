package co.orange.ddanzi.global.firebase;

import co.orange.ddanzi.domain.user.enums.FcmCase;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FirebaseUtils {
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
}
