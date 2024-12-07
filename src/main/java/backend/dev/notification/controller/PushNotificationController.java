package backend.dev.notification.controller;

import backend.dev.notification.dto.NotificationCreateDTO;
import backend.dev.notification.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/push")
@RequiredArgsConstructor
public class PushNotificationController {

    private final PushNotificationService pushNotificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendPushNotification(@RequestBody NotificationCreateDTO request) {
        try {
            String result = pushNotificationService.sendPushNotification(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 디버깅
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}
