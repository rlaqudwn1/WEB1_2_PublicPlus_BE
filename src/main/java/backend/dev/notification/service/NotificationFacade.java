package backend.dev.notification.service;

import backend.dev.notification.dto.NotificationDTO;

import backend.dev.notification.repository.NotificationRepository;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class NotificationFacade {
    private final NotificationRepository notificationRepository;
    private final ChatNotificationService chatNotificationService;
    private final ActivityNotificationService activityNotificationService;

    public void sendActivityNotification() {
        chatNotificationService.sendChatNotification();
    }
    public void sendChatNotification(){
        chatNotificationService.sendChatNotification();
    }





}
