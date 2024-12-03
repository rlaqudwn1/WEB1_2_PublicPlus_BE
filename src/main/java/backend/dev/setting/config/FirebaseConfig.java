package backend.dev.setting.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // FirebaseApp 초기화 상태 확인
        if (FirebaseApp.getApps().isEmpty()) {
            // Firebase 서비스 계정 키 파일 경로를 지정하세요.
            Resource resource = new ClassPathResource("firebase.json");
            InputStream serviceAccount = resource.getInputStream();  // InputStream으로 처리

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            return FirebaseApp.initializeApp(options);
        } else {
            // 이미 초기화된 FirebaseApp 반환
            return FirebaseApp.getInstance();
        }
    }
}
