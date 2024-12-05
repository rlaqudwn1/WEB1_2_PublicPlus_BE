package backend.dev.notification.service;

import backend.dev.notification.entity.FCMToken;
import backend.dev.notification.entity.Topic;
import backend.dev.notification.exception.NotificationException;
import backend.dev.notification.repository.TokenRepository;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;



    public void updateOrSaveToken(User user, String fcmToken) {
        if (fcmToken == null || fcmToken.isEmpty()) {
            throw NotificationException.NOT_FOUND_FCM_TOKEN.getNotificationException();
        }
        user.changeToken(fcmToken);
        userRepository.save(user);

        log.info(fcmToken);
        Optional<FCMToken> existingToken = tokenRepository.findByUser(user);

        if (existingToken.isPresent()) {
            // 기존 토큰 갱신
            existingToken.get().updateToken(fcmToken);
        } else {
            // 새로운 토큰 저장
            FCMToken newToken = FCMToken.builder()
                    .user(user)
                    .fcm_token(fcmToken)
                    .build();
            tokenRepository.save(newToken);
        }
    }
    public void topicSubscribe(User user, Topic topic) {

    }

    public boolean verifyToken(String token) {
        log.info("토큰 검증 중");
        if (token == null || token.isEmpty()) {
            return false; // 토큰이 없으면 검증 실패
        }
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            log.info("토큰 검증 성공");
            return decodedToken != null;
        } catch (FirebaseException e) {
            return false; // 토큰 검증 실패
        }
    }
}
