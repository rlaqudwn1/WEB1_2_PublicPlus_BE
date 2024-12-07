package backend.dev.notification.service;

import backend.dev.notification.dto.NotificationCreateDTO;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationService {
    private final UserRepository userRepository;

    // 푸시 알림 전송 메서드
    public String sendPushNotification(NotificationCreateDTO dto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("name? : {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));

        Message message = Message.builder()
                .setToken(user.getFcmToken())
                .setNotification(Notification.builder().setTitle(dto.getTitle())
                        .setBody(dto.getBody()).build())
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            return "푸시 알림 전송 성공: " + response;
        } catch (Exception e) {
            return "푸시 알림 전송 실패: " + e.getMessage();
        }
    }
}

