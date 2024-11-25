package backend.dev.notification.controller;

import backend.dev.notification.dto.NotificationCreateDTO;
import backend.dev.notification.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/push")
@CrossOrigin(origins = "http://localhost:3000")  // React 앱의 URL
public class PushNotificationController {

    @Autowired
    private PushNotificationService pushNotificationService;

    // 푸시 알림 전송 API
    @PostMapping("/send")
    public String sendPushNotification(@RequestBody NotificationCreateDTO request) {
        return pushNotificationService.sendPushNotification(request, "1");
    }
}
