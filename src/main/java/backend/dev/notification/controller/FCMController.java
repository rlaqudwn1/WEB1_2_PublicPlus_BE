package backend.dev.notification.controller;

import backend.dev.notification.entity.FCMToken;
import backend.dev.notification.repository.TokenRepository;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
public class FCMController {

    private final UserRepository userRepository;
    private final TokenRepository fcmTokenRepository;

    public FCMController(UserRepository userRepository, TokenRepository fcmTokenRepository) {
        this.userRepository = userRepository;
        this.fcmTokenRepository = fcmTokenRepository;
    }

    @PostMapping("/save-token")
    public ResponseEntity<String> saveToken(@RequestBody FCMTokenRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        FCMToken fcmToken = FCMToken.builder()
                .fcm_token(request.getToken())
                .user(user)
                .build();

        fcmTokenRepository.save(fcmToken);
        return ResponseEntity.ok("FCM 토큰 저장 성공");
    }
}

@Data
class FCMTokenRequest {
    private String userId;
    private String token;

    // Getter & Setter
}
