package backend.dev.testdata;

import backend.dev.notification.entity.Notification;
import backend.dev.notification.entity.NotificationTitleType;
import backend.dev.notification.repository.NotificationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NotificationInitializer {

    @Autowired
    private NotificationRepository notificationRepository;

    @PostConstruct
    public void init() {
        for (int i = 0; i < 50; i++) {
            notificationRepository.save(createNotification(i));
        }
    }

    private Notification createNotification(int index) {
        Random random = new Random();
        //enum 랜덤
        NotificationTitleType[] notificationTitleTypes = NotificationTitleType.values();
        NotificationTitleType notificationTitleType = notificationTitleTypes[random.nextInt(notificationTitleTypes.length)];
        //
        return Notification.builder()
                .title("Test Notification"+index)
                .message("Test Message"+index)
                .notificationTitleType(notificationTitleType)
                .build();
    }


}
