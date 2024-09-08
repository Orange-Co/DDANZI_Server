package co.orange.ddanzi.global.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;


@Slf4j
@Configuration
public class FirebaseConfig {
    @Value("${firebase.credentials.location}")
    private String location;

    @PostConstruct
    public void init(){
        try{
            initializeFirebaseApp();
        } catch (Exception e){
            log.info("Firebase 초기화 중 오류 발생");
            log.error(e.getMessage());
        }
    }

    @Bean
    FirebaseMessaging firebaseMessaging() throws Exception{
        if (FirebaseApp.getApps().isEmpty()) {
            initializeFirebaseApp();
        }
        return FirebaseMessaging.getInstance(FirebaseApp.getInstance());
    }

    private void initializeFirebaseApp() throws Exception {
        InputStream serviceAccount = new ClassPathResource(location).getInputStream();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);

            log.info("FirebaseApp이 성공적으로 초기화되었습니다.");
        }
    }

}
