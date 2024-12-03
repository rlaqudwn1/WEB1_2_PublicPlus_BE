package backend.dev.notification.controller;

import backend.dev.notification.dto.NotificationDTO;
import backend.dev.notification.service.PushNotificationService;
import backend.dev.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private PushNotificationService pushNotificationService;
    private UserService userService;

    @PostMapping("/send")
    public void sendPushNotification(@RequestBody NotificationDTO notificationDTO) {
        String fcmToken = userService.findMyInformation(notificationDTO.getUserId()).description();
    }
}
